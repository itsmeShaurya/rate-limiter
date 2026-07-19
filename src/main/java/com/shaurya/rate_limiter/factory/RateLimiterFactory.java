package com.shaurya.rate_limiter.factory;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.strategy.RateLimiter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RateLimiterFactory {
    private final Map<String, RateLimiter> rateLimiters;
    private final RateLimiterProperties properties;

    public RateLimiterFactory(Map<String, RateLimiter> rateLimiters, RateLimiterProperties properties) {
        this.rateLimiters = rateLimiters;
        this.properties = properties;
    }

    public RateLimiter getRateLimiter(){
        switch (properties.getAlgorithm().toLowerCase()){
            case "fixed-window":
                return rateLimiters.get("fixedWindowRateLimiter");
            case "sliding-window":
                return rateLimiters.get("slidingWindowRateLimiter");
            case "token-window":
                return rateLimiters.get("tokenBucketRateLimiter");
            default:
                throw new IllegalArgumentException(
                        "Unknown algorithm: " + properties.getAlgorithm()
                );
        }
    }
}
