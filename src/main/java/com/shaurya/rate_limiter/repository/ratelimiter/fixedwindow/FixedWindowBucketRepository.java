package com.shaurya.rate_limiter.repository.ratelimiter.fixedwindow;

import com.shaurya.rate_limiter.model.fixedwindow.FixedWindowBucket;

public interface FixedWindowBucketRepository {
    FixedWindowBucket getBucket(String userId);
    void saveBucket(String userId, FixedWindowBucket bucket);
    void removeBucket(String userId);
}
