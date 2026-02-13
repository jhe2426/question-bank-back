package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.mock.exam.GetUnitsAndChaptersResponseDto;

public interface MockExamService {
    public ResponseEntity<? super GetUnitsAndChaptersResponseDto> getUnitsAndChapters();
}
