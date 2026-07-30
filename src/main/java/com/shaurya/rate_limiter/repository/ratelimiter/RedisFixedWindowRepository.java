package com.shaurya.rate_limiter.repository.ratelimiter;

public interface RedisFixedWindowRepository {
    /**
     * Increments the request count for a user.
     *
     * @param userId User making the request.
     * @param windowSizeSeconds Size of the rate-limit window.
     * @return Updated request count.
     */
    long incrementRequestCount(String userId, long windowSizeSeconds);

    /**
     * Returns the remaining time of the current window.
     *
     * @param userId User whose window we want to check.
     * @return Remaining TTL in seconds.
     */
    long getRemainingWindow(String userId);
}
