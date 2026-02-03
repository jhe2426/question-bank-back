package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.auth.ApprovalCodeRequestDto;
import com.jhe.question_bank.common.dto.request.auth.ApprovalCodeVerifyRequestDto;
import com.jhe.question_bank.common.dto.request.auth.IdCheckRequestDto;
import com.jhe.question_bank.common.dto.request.auth.PhoneNumberAuthCodeRequestDto;
import com.jhe.question_bank.common.dto.request.auth.PhoneNumberAuthCodeVerifyRequestDto;
import com.jhe.question_bank.common.dto.request.auth.SignInRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.auth.AccessTokenRefreshResponseDto;
import com.jhe.question_bank.common.dto.response.auth.GetUniversitiesResponseDto;
import com.jhe.question_bank.common.dto.response.auth.SignInResponseDto;

public interface AuthService {
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);
    public ResponseEntity<? super AccessTokenRefreshResponseDto> refreshAccessToken(String refreshToken);
    public ResponseEntity<? super GetUniversitiesResponseDto> getUniversities();
    public ResponseEntity<ResponseDto> idCheck(IdCheckRequestDto dto);
    public ResponseEntity<ResponseDto> phoneNumberAuthCode(PhoneNumberAuthCodeRequestDto dto);
    public ResponseEntity<ResponseDto> phoneNumberAuthCodeVerify(PhoneNumberAuthCodeVerifyRequestDto dto);
    public ResponseEntity<ResponseDto> approvalCode(ApprovalCodeRequestDto dto);
    public ResponseEntity<ResponseDto> approvalCodeVerify(ApprovalCodeVerifyRequestDto dto);
    public ResponseEntity<ResponseDto> logout(String userId);
}
