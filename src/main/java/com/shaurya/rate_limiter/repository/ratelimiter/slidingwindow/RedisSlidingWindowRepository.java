package com.shaurya.rate_limiter.repository.ratelimiter.slidingwindow;

import java.util.List;

public interface RedisSlidingWindowRepository {
    // Store a request timestamp for the user.
    void addRequest(String userId, long timestamp);

    // Remove timestamps that are older than the current window.
    void removeExpiredRequests(String userId, long windowStart);

    // Get all request timestamps currently inside the window.
    List<Long> getRequestTimestamps(String userId);
}
