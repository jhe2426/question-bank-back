package com.jhe.question_bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.constant.RequestPattern;
import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
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
        ResponseEntity<? super SignInResponseDto> authResponse = authService.signIn(requestBody);

        boolean isSuccess = authResponse.getStatusCode() == HttpStatus.OK;
        if (!isSuccess) {
            return authResponse;
        }

        SignInResponseDto body = (SignInResponseDto) authResponse.getBody();
        if (body == null) return authResponse;

        String refreshCookie = authCookieProvider.refreshToken(body.getRefreshToken()).toString();
        String csrfCookie = authCookieProvider.csrfToken(body.getCsrfToken()).toString();

        return ResponseDto.successWithCookies(body, refreshCookie, csrfCookie);
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
    
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(
        @AuthenticationPrincipal String userId 
    ) {
        ResponseEntity<ResponseDto> response = authService.logout(userId);
                boolean isSuccess = response.getStatusCode() == HttpStatus.OK;

        if (!isSuccess) {
            return response;
        }

        String refreshCookie = authCookieProvider.deleteRefreshTokenCookie().toString();
        String csrfCookie = authCookieProvider.deleteCsrfTokenCookie().toString();

        ResponseDto body = response.getBody();
        return ResponseDto.successWithCookies(body, refreshCookie, csrfCookie);
    }
}
