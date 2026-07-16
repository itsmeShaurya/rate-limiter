package com.shaurya.rate_limiter.strategy;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.slidingwindow.SlidingWindowBucket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowRateLimiter implements RateLimiter{

    private final RateLimiterProperties rateLimiterProperties;

    private Map<String, SlidingWindowBucket> userBuckets = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }


    @Override
    public boolean allowRequests(String userId) {
        return false;
    }
}
