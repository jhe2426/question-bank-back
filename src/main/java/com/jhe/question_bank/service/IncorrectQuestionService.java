package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.incorrectquestion.GetIncorrectQuestionListResponseDto;

public interface IncorrectQuestionService {
    public ResponseEntity<? super GetIncorrectQuestionListResponseDto> getIncorrectQuestionList(String userId, String sourceType);
}
