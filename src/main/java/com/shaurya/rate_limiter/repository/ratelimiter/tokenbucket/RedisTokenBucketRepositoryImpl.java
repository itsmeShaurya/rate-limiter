package com.shaurya.rate_limiter.repository.ratelimiter.tokenbucket;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RedisTokenBucketRepositoryImpl implements RedisTokenBucketRepository {

    private final StringRedisTemplate redisTemplate;

    public RedisTokenBucketRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis executes this entire script atomically.
    private static final String LUA_SCRIPT = """
            local key = KEYS[1]

            local capacity = tonumber(ARGV[1])
            local refillRate = tonumber(ARGV[2])
            local currentTime = tonumber(ARGV[3])

            -- Get the current bucket state.
            local bucket = redis.call('HMGET', key, 'tokens', 'lastRefillTime')

            local tokens = tonumber(bucket[1])
            local lastRefillTime = tonumber(bucket[2])

            -- First request: initialize the bucket with full capacity.
            if tokens == nil then
                tokens = capacity
                lastRefillTime = currentTime
            end

            -- Calculate elapsed time in seconds.
            local elapsedSeconds = (currentTime - lastRefillTime) / 1000

            -- Calculate tokens that should be refilled.
            local tokensToAdd = elapsedSeconds * refillRate

            -- Refill tokens without exceeding bucket capacity.
            tokens = math.min(capacity, tokens + tokensToAdd)

            -- Check whether at least one token is available.
            if tokens >= 1 then

                -- Consume one token.
                tokens = tokens - 1

                -- Save the updated bucket state.
                redis.call('HSET', key,
                    'tokens', tokens,
                    'lastRefillTime', currentTime
                )

                return 1
            end

            -- Save the updated refill time even when request is rejected.
            redis.call('HSET', key,
                'tokens', tokens,
                'lastRefillTime', currentTime
            )

            return 0
            """;

    @Override
    public boolean tryConsume(String userId, double capacity, double refillRate) {

        String key = "token:" + userId;

        DefaultRedisScript<Long> script =
                new DefaultRedisScript<>(LUA_SCRIPT, Long.class);

        Long result = redisTemplate.execute(
                script,
                List.of(key),
                String.valueOf(capacity),
                String.valueOf(refillRate),
                String.valueOf(System.currentTimeMillis())
        );

        return result != null && result == 1;
    }
}