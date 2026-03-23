package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.past.exam.PostPastExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.grading.PostExamGradingResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamRoundsResponseDto;

public interface PastExamService {
    public ResponseEntity<? super GetPastExamRoundsResponseDto> getPastExamRounds();
    public ResponseEntity<? super GetPastExamQuestionListResponseDto> getPastExamQuestionList(Integer pastExamId);
    public ResponseEntity<? super PostExamGradingResponseDto> postPastExamGrade(String userId, PostPastExamGradingRequestDto dto);
}
