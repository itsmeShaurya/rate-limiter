package com.shaurya.rate_limiter.strategy.inmemory;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.slidingwindow.SlidingWindowBucket;
import com.shaurya.rate_limiter.repository.ratelimiter.slidingwindow.SlidingWindowBucketRepository;
import com.shaurya.rate_limiter.strategy.RateLimiter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("slidingWindowRateLimiter")
@Profile("inmemory")
public class SlidingWindowRateLimiter implements RateLimiter {

    private final RateLimiterProperties rateLimiterProperties;
    private final SlidingWindowBucketRepository repository;

    private Map<String, SlidingWindowBucket> userBuckets = new ConcurrentHashMap<>();

    public SlidingWindowRateLimiter(RateLimiterProperties rateLimiterProperties, SlidingWindowBucketRepository repository) {
        this.rateLimiterProperties = rateLimiterProperties;
        this.repository = repository;
    }


    @Override
    public boolean allowRequests(String userId) {
        // Get the user's bucket from In-Memory storage.
        SlidingWindowBucket bucket = repository.getBucket(userId);
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
                repository.saveBucket(userId, bucket);
                return true;
            }
        }
        return false;
    }
}
