package com.yutsuki.chatserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;


    /**
     * @description: set key-value pair with default expire time of 1 hour
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.HOURS);
    }

    public void set(String key, String value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean isExpiredKey(String key) {
        Long ttl = redisTemplate.getExpire(key);
        return (ttl != null && ttl == -2);
    }

    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }
}
