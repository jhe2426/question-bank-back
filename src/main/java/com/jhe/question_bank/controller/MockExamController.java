package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.dto.response.mock.exam.GetMockExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.mock.exam.GetUnitsAndChaptersResponseDto;
import com.jhe.question_bank.enums.QuestionDifficulty;
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

    @GetMapping("/{chapterId}/{difficulty}/{hasExplanation}")
    public ResponseEntity<? super GetMockExamQuestionListResponseDto> getMockExamQuestionList(
        @AuthenticationPrincipal String userId,
        @PathVariable("chapterId") Integer chapterId,
        @PathVariable("difficulty") QuestionDifficulty difficulty,
        @PathVariable("hasExplanation") Boolean hasExplanation
    ) {
        ResponseEntity<? super GetMockExamQuestionListResponseDto> response = mockExamService.getMockExamQuestionList(userId, chapterId, difficulty, hasExplanation);
        return response;
    }
}
