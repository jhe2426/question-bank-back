package com.jhe.question_bank.service;

import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.request.mock.exam.PostMockExamGradingRequestDto;
import com.jhe.question_bank.common.dto.response.grading.PostExamGradingResponseDto;
import com.jhe.question_bank.common.dto.response.mock.exam.GetMockExamQuestionListResponseDto;
import com.jhe.question_bank.common.dto.response.mock.exam.GetUnitsAndChaptersResponseDto;
import com.jhe.question_bank.enums.QuestionDifficulty;

public interface MockExamService {
    public ResponseEntity<? super GetUnitsAndChaptersResponseDto> getUnitsAndChapters();
    public ResponseEntity<? super GetMockExamQuestionListResponseDto> getMockExamQuestionList(String userId, Integer chapterId, QuestionDifficulty difficulty, Boolean hasExplanation);
    public ResponseEntity<? super PostExamGradingResponseDto> postMockExamGrade(String userId, PostMockExamGradingRequestDto dto);
}
