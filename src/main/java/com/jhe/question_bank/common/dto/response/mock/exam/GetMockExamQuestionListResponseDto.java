package com.jhe.question_bank.common.dto.response.mock.exam;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.vo.MockExamQuestionVO;

import lombok.Getter;

@Getter
public class GetMockExamQuestionListResponseDto extends ResponseDto {
    private Integer sessionId;
    private List<MockExamQuestionVO> questions;

    private GetMockExamQuestionListResponseDto(Integer sessionId, List<QuestionEntity> questionEntities, boolean hasExplanation) {
        this.sessionId = sessionId;
        this.questions = MockExamQuestionVO.getMockExamQuestionList(questionEntities, hasExplanation);
    }

    public static ResponseEntity<GetMockExamQuestionListResponseDto> success(Integer sessionId, List<QuestionEntity> questionEntities, boolean hasExplanation) {
        GetMockExamQuestionListResponseDto body = new GetMockExamQuestionListResponseDto(sessionId, questionEntities, hasExplanation);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
