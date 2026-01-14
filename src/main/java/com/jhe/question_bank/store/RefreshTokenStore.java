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

    public void save(String userId, String refreshToken) {

        try {
            redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, REFRESH_TOKEN_EXPIRE);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }

    }

    public boolean matches(String userId, String refreshToken) {
        try {

            String stored = redisTemplate.opsForValue().get("refresh:" + userId);
            boolean isRefreshTokenMatched = refreshToken.equals(stored);
            return isRefreshTokenMatched;
            
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }
    }

}
