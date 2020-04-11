package de.roamingthings.workbench.resilience4j.usecases.sum;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SumService {

    private static final Logger log = LoggerFactory.getLogger(SumService.class);

    private static final String ORIGIN_CLIENT_A = "SumClientA";
    private static final String ORIGIN_CLIENT_B = "SumClientB";

    private final SumClientA sumClientA;
    private final SumClientB sumClientB;
    private final SumCache sumCache;

    public SumResponse sum(SumRequest request) {
        log.info("---------- Handling Sum Request by resilience4j ----------");
        return Try.ofSupplier(() -> retrieveSumFromServiceA(request))
                .recover(Exception.class, e -> retrieveSumFromServiceB(request))
                .onSuccess(sumResponse -> sumCache.updateCache(request, sumResponse))
                .recover(Exception.class, e -> sumCache.lookup(request))
                .get();
    }

    public SumResponse sumSpringRetry(SumRequest request) {
        log.info("---------- Handling Sum Request by Spring Retry ----------");
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(2);

//        TimeoutRetryPolicy policy = new TimeoutRetryPolicy();
//        policy.setTimeout(1000L);

        template.setRetryPolicy(policy);

        SumResponse response = null;
        try {
            response = template.execute(
                    context -> retrieveSumFromServiceA(request),
                    context -> retrieveSumFromServiceB(request)
            );
            sumCache.updateCache(request, response);
        } catch (Exception e) {
            response = sumCache.lookup(request);
        }

        return response;
    }

    private SumResponse retrieveSumFromServiceB(SumRequest request) {

        Integer numberA = request.getNumberA();
        Integer numberB = request.getNumberB();
        return new SumResponse(
                numberA,
                numberB,
                sumClientB.sum(numberA, numberB),
                ORIGIN_CLIENT_B
        );
    }

    private SumResponse retrieveSumFromServiceA(SumRequest request) {
        Integer numberA = request.getNumberA();
        Integer numberB = request.getNumberB();
        return new SumResponse(
                numberA,
                numberB,
                sumClientA.sum(numberA, numberB),
                ORIGIN_CLIENT_A
        );
    }
}
