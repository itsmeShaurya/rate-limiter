package com.shaurya.rate_limiter.repository.ratelimiter.slidingwindow;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RedisSlidingWindowRepositoryImpl implements RedisSlidingWindowRepository{
    private final StringRedisTemplate redisTemplate;

    public RedisSlidingWindowRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addRequest(String userId, long timestamp) {
        String key = "sliding:" + userId;
        redisTemplate.opsForZSet().add(key, String.valueOf(timestamp), timestamp);
    }

    @Override
    public void removeExpiredRequests(String userId, long windowStart) {
        String key = "sliding:" + userId;
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart-1);
    }

    @Override
    public List<Long> getRequestTimestamps(String userId) {
        String key = "sliding:" + userId;
        return  redisTemplate.opsForZSet().range(key, 0, -1).stream().map(Long::valueOf).toList();
    }
}
