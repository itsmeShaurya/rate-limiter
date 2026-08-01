package com.shaurya.rate_limiter.strategy.redis;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.repository.ratelimiter.slidingwindow.RedisSlidingWindowRepository;
import com.shaurya.rate_limiter.strategy.RateLimiter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("slidingWindowRateLimiter")
@Profile("redis")
public class RedisSlidingWindowRateLimiter implements RateLimiter {
    private final RateLimiterProperties properties;
    private final RedisSlidingWindowRepository repository;

    public RedisSlidingWindowRateLimiter(RateLimiterProperties properties, RedisSlidingWindowRepository repository) {
        this.properties = properties;
        this.repository = repository;
    }

    @Override
    public boolean allowRequests(String userId) {
        long currentTime = System.currentTimeMillis();
        long windowSizeMillis = properties.getWindowSizeSeconds() * 1000;

        // Calculate the start of the valid window
        long windowStart = currentTime - windowSizeMillis;

        // Remove requests that are older than the current sliding window.
        repository.removeExpiredRequests(userId, windowStart);

        // Get all requests that are currently inside the sliding window.
        List<Long> timestamps = repository.getRequestTimestamps(userId);

        if (timestamps.size() < properties.getMaxRequests()) {
            // Add the current request to Redis.
            repository.addRequest(userId, currentTime);
            return true;
        }
        // User has exceeded the limit.
        return false;
    }
}
