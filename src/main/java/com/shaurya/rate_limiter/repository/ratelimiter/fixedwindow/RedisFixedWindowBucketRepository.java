package com.shaurya.rate_limiter.repository.ratelimiter.fixedwindow;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@Profile("redis")
public class RedisFixedWindowBucketRepository implements RedisFixedWindowRepository{
    private final StringRedisTemplate redisTemplate;

    public RedisFixedWindowBucketRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Increments the request count for the given user.
     *
     * If this is the first request in the current window,
     * a TTL (Time To Live) is set so Redis automatically
     * deletes the key when the window expires.
     *
     * @param userId User making the request.
     * @param windowSizeSeconds Window duration.
     * @return Updated request count.
     */
    @Override
    public long incrementRequestCount(String userId, long windowSizeSeconds){
        // Create a unique Redis key for this user's rate-limit window.
        String key = "fixed:"+ userId;

        // Atomically increment the request count.
        Long count = redisTemplate.opsForValue().increment(key);

        // Set the TTL only when the window starts.
        if(count != null && count == 1){
            redisTemplate.expire(key, Duration.ofSeconds(windowSizeSeconds));
        }

        // Return the updated request count.
        return count == null ? 0 : count;
    }

    /**
     * Returns the remaining TTL (Time To Live)
     * of the user's window.
     */
    @Override
    public long getRemainingWindow(String userId){
        String key = "fixed:" + userId;
        // Ask Redis how many seconds remain before the key automatically expires.
        Long ttl = redisTemplate.getExpire(key);

        // -1 means the key exists but has no expiration.
        // -2 means the key does not exist.
        return ttl == null ? -1 : ttl;
    }
}
