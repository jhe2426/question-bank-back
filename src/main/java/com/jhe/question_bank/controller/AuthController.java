package com.jhe.question_bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.constant.RequestPattern;
import com.jhe.question_bank.common.dto.request.auth.ApprovalCodeRequestDto;
import com.jhe.question_bank.common.dto.request.auth.ApprovalCodeVerifyRequestDto;
import com.jhe.question_bank.common.dto.request.auth.IdCheckRequestDto;
import com.jhe.question_bank.common.dto.request.auth.PhoneNumberAuthCodeRequestDto;
import com.jhe.question_bank.common.dto.request.auth.PhoneNumberAuthCodeVerifyRequestDto;
import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.request.auth.SignUpRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.auth.AccessTokenRefreshResponseDto;
import com.jhe.question_bank.common.dto.response.auth.GetUniversitiesResponseDto;
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

    @PostMapping("/id-check")
    public ResponseEntity<ResponseDto> idCheck(
        @RequestBody @Valid IdCheckRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = authService.idCheck(requestBody);
        return response;
    }

    @GetMapping("/universities")
    public ResponseEntity<? super GetUniversitiesResponseDto> getUniversities() {
        ResponseEntity<? super GetUniversitiesResponseDto> response = authService.getUniversities();
        return response;
    }

    @PostMapping("/phone-number/auth-code")
    public ResponseEntity<ResponseDto> phoneNumberAuthCode(
        @RequestBody @Valid PhoneNumberAuthCodeRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = authService.phoneNumberAuthCode(requestBody);
        return response;
    }

    @PostMapping("/phone-number/auth-code/verify")
    public ResponseEntity<ResponseDto> phoneNumberAuthCodeVerify(
        @RequestBody @Valid PhoneNumberAuthCodeVerifyRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = authService.phoneNumberAuthCodeVerify(requestBody);
        return response;
    }

    @PostMapping("/approval-code")
    public ResponseEntity<ResponseDto> approvalCode(
        @RequestBody @Valid ApprovalCodeRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = authService.approvalCode(requestBody);
        return response;
    }
    
    @PostMapping("/approval-code/verify")
    public ResponseEntity<ResponseDto> approvalCodeVerify(
        @RequestBody @Valid ApprovalCodeVerifyRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = authService.approvalCodeVerify(requestBody);
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signUp(
        @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = authService.signUp(requestBody);
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
