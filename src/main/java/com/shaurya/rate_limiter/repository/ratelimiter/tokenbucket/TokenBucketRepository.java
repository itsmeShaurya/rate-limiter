package com.shaurya.rate_limiter.repository.ratelimiter.tokenbucket;

import com.shaurya.rate_limiter.model.tocketbucket.TokenBucket;

public interface TokenBucketRepository {
    // Get the user's token bucket.
    TokenBucket getBucket(String userId);

    // Save the updated token bucket.
    void saveBucket(String userId, TokenBucket bucket);

    // Remove the user's bucket.
    void removeBucket(String userId);
}
