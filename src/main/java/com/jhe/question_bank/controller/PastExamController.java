package com.jhe.question_bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhe.question_bank.common.dto.request.past.exam.PostPastExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamRoundsResponseDto;
import com.jhe.question_bank.service.PastExamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/past-exam")
@RequiredArgsConstructor
public class PastExamController {
    private final PastExamService pastExamService;

    @GetMapping
    public ResponseEntity<? super GetPastExamRoundsResponseDto> getPastExamRounds() {
        ResponseEntity<? super GetPastExamRoundsResponseDto> response = pastExamService.getPastExamRounds();
        return response;
    }

    @GetMapping("/{pastExamId}")
    public ResponseEntity<? super GetPastExamQuestionListResponseDto> getPastExamQuestionList(
        @PathVariable("pastExamId") Integer pastExamId
    ) {
        ResponseEntity<? super GetPastExamQuestionListResponseDto> response = pastExamService.getPastExamQuestionList(pastExamId);
        return response;
    }   

    @PostMapping("/grading")
    public ResponseEntity<ResponseDto> postPastExamGrade(
        @AuthenticationPrincipal String userId,
        @RequestBody @Valid PostPastExamGradingRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response = pastExamService.postPastExamGrading(userId, requestBody);
        return response;
    }
}
