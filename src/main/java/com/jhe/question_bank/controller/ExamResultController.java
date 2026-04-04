package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.dto.response.examresult.GetExamResultResponseDto;
import com.jhe.question_bank.service.ExamResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/exam-result")
@RequiredArgsConstructor
public class ExamResultController {
    
    private final ExamResultService examResultService;

    @GetMapping("/{groupId}/{filter}")
    public ResponseEntity<? super GetExamResultResponseDto> getExamResult(
        @AuthenticationPrincipal String userId,
        @PathVariable("groupId") Integer groupId,
        @PathVariable("filter") String filter
    ) {
        ResponseEntity<? super GetExamResultResponseDto> response = examResultService.getExamResult(userId, groupId, filter);
        return response;
    }

}
