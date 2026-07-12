package com.shaurya.rate_limiter.service;


import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.UserBucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private RateLimiterProperties rateLimiterProperties;

    public RateLimiterService(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }

    private final Map<String, UserBucket> userBuckets = new ConcurrentHashMap<>();

    public boolean allowRequests(String userId){
        UserBucket bucket = userBuckets.computeIfAbsent(userId, key -> new UserBucket());

        long currentTime = System.currentTimeMillis();
        long windowSizeMillis = rateLimiterProperties.getWindowSizeSeconds() * 1000;

        if(currentTime - bucket.getWindowTime() >= windowSizeMillis){
            bucket.setRequestCount(0);
            bucket.setWindowTime(currentTime);
        }

        if(bucket.getRequestCount() < rateLimiterProperties.getMaxRequests()){
            bucket.setRequestCount(bucket.getRequestCount() + 1);
            return true;
        }
        return false;
    }
}
