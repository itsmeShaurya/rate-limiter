package com.shaurya.rate_limiter.service;


import com.shaurya.rate_limiter.model.UserBucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    public static final int MAX_REQUESTS = 5;
    public static final long WINDOW_SIZE = 60 * 1000;

    private final Map<String, UserBucket> userBuckets = new ConcurrentHashMap<>();

    public boolean allowRequests(String userId){
        UserBucket bucket = userBuckets.computeIfAbsent(userId, key -> new UserBucket());

        long currentTime = System.currentTimeMillis();

        if(currentTime - bucket.getWindowTime() >= WINDOW_SIZE){
            bucket.setRequestCount(0);
            bucket.setWindowTime(currentTime);
        }

        if(bucket.getRequestCount() < MAX_REQUESTS){
            bucket.setRequestCount(bucket.getRequestCount() + 1);
            return true;
        }
        return false;
    }
}
