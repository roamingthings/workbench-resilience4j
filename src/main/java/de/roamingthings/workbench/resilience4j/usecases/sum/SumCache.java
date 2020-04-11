package de.roamingthings.workbench.resilience4j.usecases.sum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SumCache {
    private static final Logger log = LoggerFactory.getLogger(SumCache.class);

    private static final String ORIGIN_SUM_CACHE = "ORIGIN_SUM_CACHE";
    private Map<SumRequest, SumResponse> cache = new HashMap<>();

    public SumResponse lookup(SumRequest sumRequest) {
        log.info("🔍 SumCache lookup");
        return cache.getOrDefault(
                sumRequest,
                new SumResponse(
                        -1,
                        -1,
                        -1,
                        "CACHE_MISS"
                )
        );
    }

    public void updateCache(SumRequest sumRequest, SumResponse sumResponse) {
        log.info("🗃 SumCache update from origin {}", sumResponse.getOrigin());
        cache.putIfAbsent(sumRequest,
                new SumResponse(
                        sumResponse.getNumberA(),
                        sumResponse.getNumberB(),
                        sumResponse.getSum(),
                        ORIGIN_SUM_CACHE
                )
        );
    }
}
