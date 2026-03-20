package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.past.exam.PostPastExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamRoundsResponseDto;

public interface PastExamService {
    public ResponseEntity<? super GetPastExamRoundsResponseDto> getPastExamRounds();
    public ResponseEntity<? super GetPastExamQuestionListResponseDto> getPastExamQuestionList(Integer pastExamId);
    public ResponseEntity<ResponseDto> postPastExamGrade(String userId, PostPastExamGradingRequestDto dto);
}
