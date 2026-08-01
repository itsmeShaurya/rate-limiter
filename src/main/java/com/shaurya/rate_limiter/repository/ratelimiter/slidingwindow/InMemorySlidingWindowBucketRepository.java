package com.shaurya.rate_limiter.repository.ratelimiter.slidingwindow;

import com.shaurya.rate_limiter.model.slidingwindow.SlidingWindowBucket;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySlidingWindowBucketRepository implements SlidingWindowBucketRepository{
    private final Map<String, SlidingWindowBucket> userBuckets = new ConcurrentHashMap<>();
    @Override
    public SlidingWindowBucket getBucket(String userId) {
        return userBuckets.computeIfAbsent(userId, key -> new SlidingWindowBucket());
    }

    @Override
    public void saveBucket(String userId, SlidingWindowBucket bucket) {
        userBuckets.put(userId, bucket);
    }

    @Override
    public void removeBucket(String userId) {
        userBuckets.remove(userId);
    }
}
