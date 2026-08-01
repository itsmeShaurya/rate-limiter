package com.shaurya.rate_limiter.strategy.redis;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.repository.ratelimiter.fixedwindow.RedisFixedWindowRepository;
import com.shaurya.rate_limiter.strategy.RateLimiter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("fixedWindowRateLimiter")
@Profile("redis")
public class RedisFixedWindowRateLimiter implements RateLimiter {
    private final RateLimiterProperties rateLimiterProperties;
    private final RedisFixedWindowRepository repository;

    public RedisFixedWindowRateLimiter(RateLimiterProperties rateLimiterProperties, RedisFixedWindowRepository repository) {
        this.rateLimiterProperties = rateLimiterProperties;
        this.repository = repository;
    }

    @Override
    public boolean allowRequests(String userId) {
        // Increment the user's request count in Redis.
        long requestCount = repository.incrementRequestCount(userId,rateLimiterProperties.getWindowSizeSeconds());
        // Allow the request if the user has not exceeded
        return requestCount <= rateLimiterProperties.getMaxRequests();
    }
}
