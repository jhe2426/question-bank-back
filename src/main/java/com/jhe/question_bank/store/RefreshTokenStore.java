package com.jhe.question_bank.store;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.jhe.question_bank.common.exception.RedisStorageException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshTokenStore {
    
    private final StringRedisTemplate redisTemplate;
    private final Duration REFRESH_TOKEN_EXPIRE = Duration.ofDays(1);

    private static final String PREFIX = "refresh:";

    private String getKey(String userId) {
        return PREFIX + userId;
    }

    public void save(String userId, String refreshToken) {

        try {
            String key = getKey(userId);
            redisTemplate.opsForValue().set(key, refreshToken, REFRESH_TOKEN_EXPIRE);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }

    }

    public boolean matches(String userId, String refreshToken) {
        try {
            String key = getKey(userId);
            String storedRefreshToken = redisTemplate.opsForValue().get(key);
            if(storedRefreshToken == null) return false;

            boolean isRefreshTokenMatched = refreshToken.equals(storedRefreshToken);
            return isRefreshTokenMatched;
            
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }
    }

    public void delete(String userId) {
        try {
            String key = getKey(userId);
            redisTemplate.delete(key);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }
    }
}
