package com.shaurya.rate_limiter.strategy.redis;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.repository.ratelimiter.tokenbucket.RedisTokenBucketRepository;
import com.shaurya.rate_limiter.strategy.RateLimiter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("tokenBucketRateLimiter")
@Profile("redis")
public class RedisTokenBucketRateLimiter implements RateLimiter {
    private final RateLimiterProperties properties;
    private final RedisTokenBucketRepository repository;

    public RedisTokenBucketRateLimiter(RateLimiterProperties properties, RedisTokenBucketRepository repository) {
        this.properties = properties;
        this.repository = repository;
    }

    @Override
    public boolean allowRequests(String userId) {
        return repository.tryConsume(
                userId,
                properties.getBucketCapacity(),
                properties.getRefillRate()
        );
    }
}
