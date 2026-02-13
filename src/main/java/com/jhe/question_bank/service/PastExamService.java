package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.past.exam.GetPastExamRoundsResponseDto;

public interface PastExamService {
    public ResponseEntity<? super GetPastExamRoundsResponseDto> getPastExamRounds();
}
