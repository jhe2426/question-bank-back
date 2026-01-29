package com.jhe.question_bank.store;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.jhe.question_bank.common.exception.RedisStorageException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhoneNumberAuthStore {
    
    private final StringRedisTemplate redisTemplate;
    private static final Duration PHONE_NUMBER_AUTH_EXPIRE = Duration.ofMinutes(5);
    private static final Duration VERIFIED_EXPIRE = Duration.ofMinutes(30);
    private static final String AUTH_PREFIX = "phone-number-auth:";
    private static final String VERIFIED_PREFIX = "verified:";

    private String getAuthKey(String phoneNumber) {
        return AUTH_PREFIX + phoneNumber;
    }

    private String getVerifiedKey(String phoneNumber) {
        return VERIFIED_PREFIX + phoneNumber;
    }

    public boolean authCodeExists(String phoneNumber) {
        try {

            Boolean hasKey = redisTemplate.hasKey(getAuthKey(phoneNumber));
            return Boolean.TRUE.equals(hasKey);

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }
    }

    public void saveAuthCode(String phoneNumber, String authCode) {
        try {
            
            redisTemplate.opsForValue().set(getAuthKey(phoneNumber), authCode, PHONE_NUMBER_AUTH_EXPIRE);

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }
    }

    public boolean isAuthCodeValid(String phoneNumber, String authCode) {
        try {
            
            String storedAuthCode = redisTemplate.opsForValue().get(getAuthKey(phoneNumber));
            if (storedAuthCode == null) return false;

            boolean isAuthCodeMatched = storedAuthCode.equals(authCode);
            return isAuthCodeMatched;

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }
    }

    public void verifySave(String phoneNumber) {
        try {
            
            redisTemplate.opsForValue().set(getVerifiedKey(phoneNumber), "true", VERIFIED_EXPIRE);
            redisTemplate.delete(getAuthKey(phoneNumber));

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }
    }

    public boolean isVerified(String phoneNumber) {
        try {
            
            Boolean hasKey = redisTemplate.hasKey(getVerifiedKey(phoneNumber));
            return Boolean.TRUE.equals(hasKey);

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RedisStorageException();
        }
    }
}
