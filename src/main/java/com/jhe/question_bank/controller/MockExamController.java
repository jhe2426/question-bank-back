package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.dto.response.mock.exam.GetUnitsAndChaptersResponseDto;
import com.jhe.question_bank.service.MockExamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/mock-exam")
@RequiredArgsConstructor
public class MockExamController {
    private final MockExamService mockExamService;

    @GetMapping("/units-with-chapters")
    public ResponseEntity<? super GetUnitsAndChaptersResponseDto> getUnitsAndChapters() {
        ResponseEntity<? super GetUnitsAndChaptersResponseDto> response = mockExamService.getUnitsAndChapters();
        return response;
    }
}
