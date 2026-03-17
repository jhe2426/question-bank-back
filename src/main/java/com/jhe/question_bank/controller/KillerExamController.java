package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.dto.response.killer.exam.GetKillerExamQuestionListResponseDto;
import com.jhe.question_bank.service.KillerExamService;

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
}
