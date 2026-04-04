package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.incorrectquestions.PostIncorrectQuestionGradingRequestDto;
import com.jhe.question_bank.common.dto.response.grading.PostExamGradingResponseDto;
import com.jhe.question_bank.common.dto.response.incorrectquestions.GetIncorrectQuestionListResponseDto;

public interface IncorrectQuestionService {
    public ResponseEntity<? super GetIncorrectQuestionListResponseDto> getIncorrectQuestionList(String userId, String sourceType);
    
    public ResponseEntity<? super PostExamGradingResponseDto> postIncorrectQuestionGrade(String userId, PostIncorrectQuestionGradingRequestDto dto);
}
