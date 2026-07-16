package com.shaurya.rate_limiter.factory;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.strategy.FixedWindowRateLimiter;
import com.shaurya.rate_limiter.strategy.RateLimiter;
import com.shaurya.rate_limiter.strategy.SlidingWindowRateLimiter;


public class RateLimiterFactory {
    private final FixedWindowRateLimiter fixedWindowRateLimiter;
    private final SlidingWindowRateLimiter slidingWindowRateLimiter;
    private final RateLimiterProperties properties;

    public RateLimiterFactory(FixedWindowRateLimiter fixedWindowRateLimiter, SlidingWindowRateLimiter slidingWindowRateLimiter, RateLimiterProperties properties) {
        this.fixedWindowRateLimiter = fixedWindowRateLimiter;
        this.slidingWindowRateLimiter = slidingWindowRateLimiter;
        this.properties = properties;
    }

    public RateLimiter getRateLimiter(){
        switch (properties.getAlgorithm().toLowerCase()){
            case "fixed-window":
                return fixedWindowRateLimiter;
            case "sliding-window":
                return slidingWindowRateLimiter;
            default:
                throw new IllegalArgumentException(
                        "Unknown algorithm: " + properties.getAlgorithm()
                );
        }
    }
}
