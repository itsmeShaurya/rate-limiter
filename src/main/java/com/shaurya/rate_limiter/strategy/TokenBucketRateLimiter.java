package com.shaurya.rate_limiter.strategy;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.tocketbucket.TokenBucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBucketRateLimiter implements RateLimiter{
    private final RateLimiterProperties properties;

    public TokenBucketRateLimiter(RateLimiterProperties properties) {
        this.properties = properties;
    }

    private final Map<String, TokenBucket> userBuckets  = new ConcurrentHashMap<>();

    @Override
    public boolean allowRequests(String userId) {
        // Get the existing bucket for the user.
        // If this is the user's first request, create a new bucket.
        // A new user starts with a full bucket.
        TokenBucket bucket = userBuckets.computeIfAbsent(userId, key -> new TokenBucket(
                properties.getBucketCapacity(),
                System.currentTimeMillis()));

        // Lock only this user's bucket so multiple requests
        // from the same user don't corrupt the token count.
        synchronized (bucket){
            // Get the current time in milliseconds.
            long currentTime = System.currentTimeMillis();

            // Calculate how much time has passed since the bucket was last refilled.
            long elapsedTime = currentTime - bucket.getLastRefillTime();

            // Convert milliseconds to seconds because our refill rate is configured as "tokens per second".
            double elapsedSeconds = elapsedTime/1000.0;

            // Calculate how many new tokens should be added.
            double tokensToAdd = elapsedSeconds * properties.getRefillRate();

            // Add the new tokens without exceeding the bucket capacity.
            bucket.setAvailableTokens(Math.min(properties.getBucketCapacity(), bucket.getAvailableTokens() + tokensToAdd));

            // Update the last refill time so future calculations
            // only consider the time after this refill.
            bucket.setLastRefillTime(currentTime);

            // If at least one token is available, allow the request.
            if(bucket.getAvailableTokens() >= 1){
                bucket.setAvailableTokens(bucket.getAvailableTokens() - 1);
                return true;
            }

            // No tokens are available, so reject the request.
            return false;
        }
    }
}
