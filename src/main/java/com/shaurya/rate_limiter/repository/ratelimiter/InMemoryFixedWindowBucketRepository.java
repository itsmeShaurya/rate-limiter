package com.shaurya.rate_limiter.repository.ratelimiter;

import com.shaurya.rate_limiter.model.fixedwindow.FixedWindowBucket;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("memory")
public class InMemoryFixedWindowBucketRepository implements FixedWindowBucketRepository{
    private final Map<String, FixedWindowBucket> userBuckets = new ConcurrentHashMap<>();
    @Override
    public FixedWindowBucket getBucket(String userId) {
        return userBuckets.computeIfAbsent(userId, key-> new FixedWindowBucket());
    }

    @Override
    public void saveBucket(String userId, FixedWindowBucket bucket) {
        userBuckets.put(userId, bucket);
    }

    @Override
    public void removeBucket(String userId) {
        userBuckets.remove(userId);
    }
}
