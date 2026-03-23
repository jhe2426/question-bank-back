package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.dto.request.killer.exam.PostKillerExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.grading.PostExamGradingResponseDto;
import com.jhe.question_bank.common.dto.response.killer.exam.GetKillerExamQuestionListResponseDto;
import com.jhe.question_bank.service.KillerExamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/killer-exam")
@RequiredArgsConstructor
public class KillerExamController {
    private final KillerExamService killerExamService;

    @GetMapping("")
    public ResponseEntity<? super GetKillerExamQuestionListResponseDto> getKillerExamQuestionList(
        @AuthenticationPrincipal String userId
    ) {
        ResponseEntity<? super GetKillerExamQuestionListResponseDto> response = killerExamService.getKillerExamQuestionList(userId);
        return response;
    }

    @PostMapping("/grading")
    public ResponseEntity<? super PostExamGradingResponseDto> postKillerExamGrade(
        @AuthenticationPrincipal String userId,
        @RequestBody @Valid PostKillerExamGradingRequestDto requestBody
    ) {
        ResponseEntity<? super PostExamGradingResponseDto> response = killerExamService.postPastExamGrade(userId, requestBody);
        return response;
    }
}
