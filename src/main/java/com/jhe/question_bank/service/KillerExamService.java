package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.killer.exam.PostKillerExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.grading.PostExamGradingResponseDto;
import com.jhe.question_bank.common.dto.response.killer.exam.GetKillerExamQuestionListResponseDto;

public interface KillerExamService {
    public ResponseEntity<? super GetKillerExamQuestionListResponseDto> getKillerExamQuestionList(String userId);
    public ResponseEntity<? super PostExamGradingResponseDto> postPastExamGrade(String userId, PostKillerExamGradingRequestDto dto);
}
