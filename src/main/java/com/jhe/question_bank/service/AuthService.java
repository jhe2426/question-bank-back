package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.auth.IdCheckRequestDto;
import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.auth.AccessTokenRefreshResponseDto;
import com.jhe.question_bank.common.dto.response.auth.SignInResponseDto;

public interface AuthService {
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);
    public ResponseEntity<? super AccessTokenRefreshResponseDto> refreshAccessToken(String refreshToken);
    public ResponseEntity<ResponseDto> idCheck(IdCheckRequestDto dto);
    public ResponseEntity<ResponseDto> logout(String userId);
}
