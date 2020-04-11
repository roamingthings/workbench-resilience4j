package de.roamingthings.workbench.resilience4j.usecases.sum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Random;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
public class SumClientB implements SumClient {
    private static final Logger log = LoggerFactory.getLogger(SumClientB.class);

    private final Random random = new Random();

    @Override
    public int sum(int a, int b) {
        failRandomly();
        log.info("📱 SumClientB is calculating {} + {}", a, b);
        return a + b;
    }

    private void failRandomly() {
        if (random.nextBoolean()) {
            log.info("💥 SumClientB");
            throw new HttpClientErrorException(INTERNAL_SERVER_ERROR);
        }
    }
}
