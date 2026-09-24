package com.devvault.devvault.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenService(
            RedisTemplate<String, String> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public void storeRefreshToken(String token, Long userId) {
        String key = "refresh:" + token;

        redisTemplate.opsForValue().set(
                key,
                userId.toString(),
                7,
                TimeUnit.DAYS
        );
    }

    public Long getUserId(String token) {
        String key = "refresh:" + token;

        String userId = redisTemplate.opsForValue().get(key);

        if (userId == null) {
            return null;
        }

        return Long.parseLong(userId);
    }

    public void deleteRefreshToken(String token) {
        String key = "refresh:" + token;

        redisTemplate.delete(key);
    }
}