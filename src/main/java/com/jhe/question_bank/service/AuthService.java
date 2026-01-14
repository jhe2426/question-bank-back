package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.response.auth.SignInResponseDto;

public interface AuthService {
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);
}
