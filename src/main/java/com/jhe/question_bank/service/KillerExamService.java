package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.killer.exam.PostKillerExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.killer.exam.GetKillerExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.killer.exam.PostKillerExamGradingResponseDto;

public interface KillerExamService {
    public ResponseEntity<? super GetKillerExamQuestionListResponseDto> getKillerExamQuestionList(String userId);
    public ResponseEntity<? super PostKillerExamGradingResponseDto> postPastExamGrade(String userId, PostKillerExamGradingRequestDto dto);
}
