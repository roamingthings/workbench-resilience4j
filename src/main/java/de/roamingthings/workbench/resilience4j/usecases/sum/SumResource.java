package de.roamingthings.workbench.resilience4j.usecases.sum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class SumResource {

    private final SumService sumService;

    @PutMapping(value = "/sum", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SumResponse sum(@RequestBody SumRequest sumRequest) {
        return sumService.sum(sumRequest);
    }

    @PutMapping(value = "/sum-spring-retry", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SumResponse sumSpringRetry(@RequestBody SumRequest sumRequest) {
        return sumService.sumSpringRetry(sumRequest);
    }
}
