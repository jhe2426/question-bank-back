package com.jhe.question_bank.provider;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.jhe.question_bank.common.constant.RequestPattern;

@Component
public class AuthCookieProvider {
    
    public ResponseCookie refreshToken(String token) {
        return ResponseCookie.from("refreshToken", token)
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path(RequestPattern.AUTH_API)
            .maxAge(Duration.ofDays(1))
            .build();
    }

    public ResponseCookie csrfToken(String token) {
        return ResponseCookie.from("csrfToken", token)
            .httpOnly(false)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(Duration.ofDays(1))
            .build();
    }

    public ResponseCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path(RequestPattern.AUTH_API)
            .maxAge(0)
            .build();
    }

    public ResponseCookie deleteCsrfTokenCookie() {
        return ResponseCookie.from("csrfToken", "")
            .httpOnly(false)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(0)
            .build();
    }
}
