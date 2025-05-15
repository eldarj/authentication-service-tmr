
package com.ejahijagic.auth.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration WINDOW = Duration.ofMinutes(30);

    private final StringRedisTemplate redis;

    public RateLimitService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public boolean isAllowed(String phone) {
        String key = "rate:" + phone;
        Long attempts = redis.opsForValue().increment(key);
        if (attempts == 1) {
            redis.expire(key, WINDOW);
        }
        return attempts <= MAX_ATTEMPTS;
    }

    public long getTtl(String phone) {
        return redis.getExpire("rate:" + phone);
    }
}
