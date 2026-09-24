package com.devvault.devvault.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public PasswordResetTokenService(
            RedisTemplate<String, String> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }

    public void storePasswordResetToken(
            String token,
            Long userId
    ) {
        String key = "password-reset:" + token;

        redisTemplate.opsForValue().set(
                key,
                userId.toString(),
                15,
                TimeUnit.MINUTES
        );
    }

    public Long getUserId(String token) {
        String key = "password-reset:" + token;

        String userId = redisTemplate.opsForValue().get(key);

        if (userId == null) {
            return null;
        }

        return Long.parseLong(userId);
    }

    public void deletePasswordResetToken(String token) {
        String key = "password-reset:" + token;

        redisTemplate.delete(key);
    }
}