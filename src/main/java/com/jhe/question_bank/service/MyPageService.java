package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.mypage.PatchUserPasswordRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;

public interface MyPageService {
    public ResponseEntity<ResponseDto> patchUserPassword(String userId, PatchUserPasswordRequestDto dto);
}