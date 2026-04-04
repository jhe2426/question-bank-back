package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.examresult.GetExamResultResponseDto;

public interface ExamResultService {
    
    public ResponseEntity<? super GetExamResultResponseDto> getExamResult(String userId, Integer groupId, String filter);

}
