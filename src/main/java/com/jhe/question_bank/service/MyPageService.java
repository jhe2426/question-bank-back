package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.mypage.PatchUserPasswordRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.mypage.GetLearningRecordListResponseDto;
import com.jhe.question_bank.common.dto.response.mypage.GetUnitGradingResultResponseDto;

public interface MyPageService {
    public ResponseEntity<ResponseDto> patchUserPassword(String userId, PatchUserPasswordRequestDto dto);
    public ResponseEntity<? super GetLearningRecordListResponseDto> getLearningRecordList(String userId); 
    public ResponseEntity<? super GetUnitGradingResultResponseDto> getUnitGradingResult(String userId, Integer groupId);
}