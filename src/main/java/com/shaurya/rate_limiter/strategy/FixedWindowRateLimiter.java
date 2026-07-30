package com.shaurya.rate_limiter.strategy;


import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.fixedwindow.FixedWindowBucket;
import com.shaurya.rate_limiter.repository.ratelimiter.FixedWindowBucketRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("fixedWindowRateLimiter")
@Profile("memory")
public class FixedWindowRateLimiter implements RateLimiter {

    private final RateLimiterProperties rateLimiterProperties;
    private final FixedWindowBucketRepository repository;

    public FixedWindowRateLimiter(RateLimiterProperties rateLimiterProperties, FixedWindowBucketRepository repository) {
        this.rateLimiterProperties = rateLimiterProperties;
        this.repository = repository;
    }

    @Override
    public boolean allowRequests(String userId) {
        FixedWindowBucket bucket = repository.getBucket(userId);

        synchronized (bucket) {

            long currentTime = System.currentTimeMillis();
            long windowSizeMillis = rateLimiterProperties.getWindowSizeSeconds() * 1000;

            if (currentTime - bucket.getWindowStartTime() >= windowSizeMillis) {
                bucket.getRequestCount().set(0);
                bucket.setWindowStartTime(currentTime);
                repository.saveBucket(userId, bucket);
            }

            if (bucket.getRequestCount().get() < rateLimiterProperties.getMaxRequests()) {
                bucket.getRequestCount().incrementAndGet();
                repository.saveBucket(userId, bucket);
                return true;
            }
            return false;

        }
    }
}
