package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.dto.request.incorrectquestions.PostIncorrectQuestionGradingRequestDto;
import com.jhe.question_bank.common.dto.response.grading.PostExamGradingResponseDto;
import com.jhe.question_bank.common.dto.response.incorrectquestions.GetIncorrectQuestionListResponseDto;
import com.jhe.question_bank.service.IncorrectQuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/incorrect-questions")
@RequiredArgsConstructor
public class IncorrectQuestionController {
    private final IncorrectQuestionService incorrectQuestionService;

    @GetMapping("/{sourceType}")
    public ResponseEntity<? super GetIncorrectQuestionListResponseDto> getIncorrectQuestionList(
        @AuthenticationPrincipal String userId,
        @PathVariable("sourceType") String sourceType
    ) {
        ResponseEntity<? super GetIncorrectQuestionListResponseDto> response = incorrectQuestionService.getIncorrectQuestionList(userId, sourceType);
        return response;
    }

    @PostMapping("/grading")
    public ResponseEntity<? super PostExamGradingResponseDto> postIncorrectQuestionGrade(
        @AuthenticationPrincipal String userId,
        @RequestBody @Valid PostIncorrectQuestionGradingRequestDto requestBody
    ) {
        ResponseEntity<? super PostExamGradingResponseDto> response = incorrectQuestionService.postIncorrectQuestionGrade(userId, requestBody);
        return response;
    }
}
