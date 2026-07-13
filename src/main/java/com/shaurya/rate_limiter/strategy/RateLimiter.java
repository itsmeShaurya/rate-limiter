package com.shaurya.rate_limiter.strategy;

public interface RateLimiter {
    boolean allowRequests(String userId);
}
