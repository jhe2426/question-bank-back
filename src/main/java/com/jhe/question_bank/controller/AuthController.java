package com.jhe.question_bank.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.constant.RequestPattern;
import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.response.auth.AccessTokenRefreshResponseDto;
import com.jhe.question_bank.common.dto.response.auth.SignInResponseDto;
import com.jhe.question_bank.common.validator.CsrfValidator;
import com.jhe.question_bank.provider.AuthCookieProvider;
import com.jhe.question_bank.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(RequestPattern.AUTH_API)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthCookieProvider authCookieProvider;
    private final CsrfValidator csrfValidator;

    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn(
        @RequestBody @Valid SignInRequestDto requestBody
    ) {
        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);

        boolean isSuccess = response.getStatusCode() == HttpStatus.OK;
        if (!isSuccess) {
            return response;
        }

        SignInResponseDto body = (SignInResponseDto) response.getBody();
        if (body == null) return response;

        String refreshCookie = authCookieProvider.refreshToken(body.getRefreshToken()).toString();
        String csrfCookie = authCookieProvider.csrfToken(body.getCsrfToken()).toString();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshCookie)
                .header(HttpHeaders.SET_COOKIE, csrfCookie)
                .body(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<? super AccessTokenRefreshResponseDto> refreshAccessToken(
        HttpServletRequest request,
        @CookieValue("refreshToken") String refreshToken
    ) {
        csrfValidator.validate(request);

        ResponseEntity<? super AccessTokenRefreshResponseDto> response = authService.refreshAccessToken(refreshToken);

        return response;
    }
    
}
