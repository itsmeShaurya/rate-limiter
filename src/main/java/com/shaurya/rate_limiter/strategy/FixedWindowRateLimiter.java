package com.shaurya.rate_limiter.strategy;


import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.fixedwindow.FixedWindowBucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FixedWindowRateLimiter implements RateLimiter {

    private final RateLimiterProperties rateLimiterProperties;

    public FixedWindowRateLimiter(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }

    private final Map<String, FixedWindowBucket> userBuckets = new ConcurrentHashMap<>();

    @Override
    public boolean allowRequests(String userId) {
        FixedWindowBucket bucket = userBuckets.computeIfAbsent(userId, key -> new FixedWindowBucket());

        synchronized (bucket) {

            long currentTime = System.currentTimeMillis();
            long windowSizeMillis = rateLimiterProperties.getWindowSizeSeconds() * 1000;

            if (currentTime - bucket.getWindowStartTime() >= windowSizeMillis) {
                bucket.getRequestCount().set(0);
                bucket.setWindowStartTime(currentTime);
            }

            if (bucket.getRequestCount().get() < rateLimiterProperties.getMaxRequests()) {
                bucket.getRequestCount().incrementAndGet();
                return true;
            }
            return false;

        }
    }
}
