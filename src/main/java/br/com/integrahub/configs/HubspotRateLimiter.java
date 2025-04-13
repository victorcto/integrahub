package br.com.integrahub.configs;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class HubspotRateLimiter {
    private final Bucket bucket;
    private static final Bandwidth BANDWIDTH = Bandwidth.builder()
            .capacity(110)
            .refillIntervally(110, Duration.ofSeconds(10))
            .build();

    public HubspotRateLimiter() {
        this.bucket = Bucket.builder().addLimit(BANDWIDTH).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}
