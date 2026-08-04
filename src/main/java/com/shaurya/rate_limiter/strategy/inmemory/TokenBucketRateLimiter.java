package com.shaurya.rate_limiter.strategy.inmemory;

import com.shaurya.rate_limiter.config.RateLimiterProperties;
import com.shaurya.rate_limiter.model.tocketbucket.TokenBucket;
import com.shaurya.rate_limiter.repository.ratelimiter.tokenbucket.TokenBucketRepository;
import com.shaurya.rate_limiter.strategy.RateLimiter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("tokenBucketRateLimiter")
@Profile("inmemory")
public class TokenBucketRateLimiter implements RateLimiter {
    private final RateLimiterProperties properties;
    private final TokenBucketRepository repository;

    public TokenBucketRateLimiter(RateLimiterProperties properties, TokenBucketRepository repository) {
        this.properties = properties;
        this.repository = repository;
    }

    @Override
    public boolean allowRequests(String userId) {
        // Get the user's bucket from the repository.
        TokenBucket bucket = repository.getBucket(userId);

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
                repository.saveBucket(userId, bucket);
                return true;
            }

            // No tokens are available, so reject the request.
            return false;
        }
    }
}
