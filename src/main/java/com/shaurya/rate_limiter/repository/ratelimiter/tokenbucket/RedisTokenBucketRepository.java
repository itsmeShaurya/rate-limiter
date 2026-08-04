package com.shaurya.rate_limiter.repository.ratelimiter.tokenbucket;

public interface RedisTokenBucketRepository {
    /**
     * Checks whether a request can be allowed
     * and updates the user's token bucket atomically.
     *
     * @param userId User making the request.
     * @param capacity Maximum number of tokens.
     * @param refillRate Tokens added per second.
     * @return true if a token was available and consumed.
     */
    boolean tryConsume(
            String userId,
            double capacity,
            double refillRate
    );
}
