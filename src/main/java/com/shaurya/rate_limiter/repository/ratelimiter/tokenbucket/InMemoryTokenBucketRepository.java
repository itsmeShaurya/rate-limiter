package com.shaurya.rate_limiter.repository.ratelimiter.tokenbucket;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.tocketbucket.TokenBucket;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTokenBucketRepository implements TokenBucketRepository{
    private final Map<String, TokenBucket> userBuckets = new ConcurrentHashMap<>();

    private final RateLimiterProperties properties;

    public InMemoryTokenBucketRepository(RateLimiterProperties properties) {
        this.properties = properties;
    }

    @Override
    public TokenBucket getBucket(String userId) {
        // If the user already has a bucket, return it.
        // Otherwise, create a new bucket with the configured capacity.
        return userBuckets.computeIfAbsent(userId, key -> new TokenBucket(
                properties.getBucketCapacity(),
                System.currentTimeMillis()
        ));
    }

    @Override
    public void saveBucket(String userId, TokenBucket bucket) {
        // Store the updated bucket for this user.
        userBuckets.put(userId, bucket);
    }

    @Override
    public void removeBucket(String userId) {
        // Remove the user's bucket.
        userBuckets.remove(userId);
    }
}
