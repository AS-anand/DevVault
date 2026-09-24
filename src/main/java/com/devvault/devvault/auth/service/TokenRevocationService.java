package com.devvault.devvault.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenRevocationService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenRevocationService(
            RedisTemplate<String, String> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public void revokeToken(String tokenId, long remainingTime) {
        String key = "revoked:" + tokenId;

        redisTemplate.opsForValue().set(
                key,
                "true",
                remainingTime,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isTokenRevoked(String tokenId) {
        String key = "revoked:" + tokenId;

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }
}