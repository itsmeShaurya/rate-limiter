package com.shaurya.rate_limiter.strategy;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.slidingwindow.SlidingWindowBucket;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SlidingWindowRateLimiter implements RateLimiter{

    private final RateLimiterProperties rateLimiterProperties;

    private Map<String, SlidingWindowBucket> userBuckets = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }


    @Override
    public boolean allowRequests(String userId) {
        SlidingWindowBucket bucket = userBuckets.computeIfAbsent(userId, key -> new SlidingWindowBucket());
        synchronized (bucket){
            long currentTime = System.currentTimeMillis();
            long windowSizeMillis = rateLimiterProperties.getWindowSizeSeconds() * 1000;
            
            // Calculate the start of the valid window
            long windowStart = currentTime - windowSizeMillis;

            Deque<Long> timestamps = bucket.getRequestTimeStamps();

            // Remove all expired requests
            while(!timestamps.isEmpty() && timestamps.peekFirst() < windowStart){
                timestamps.removeFirst();
            }

            // Check if user can make another request
            if(timestamps.size() < rateLimiterProperties.getMaxRequests()){
                timestamps.addLast(currentTime);
                return true;
            }
        }
        return false;
    }
}
