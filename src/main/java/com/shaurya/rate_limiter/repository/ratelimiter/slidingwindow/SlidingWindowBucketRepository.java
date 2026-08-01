package com.shaurya.rate_limiter.repository.ratelimiter.slidingwindow;

import com.shaurya.rate_limiter.model.slidingwindow.SlidingWindowBucket;

import java.util.List;

public interface SlidingWindowBucketRepository {
    // Get the user's sliding-window bucket.
    SlidingWindowBucket getBucket(String userId);

    // Save/update the user's bucket.
    void saveBucket(String userId, SlidingWindowBucket bucket);

    // Remove the user's bucket.
    void removeBucket(String userId);
}
