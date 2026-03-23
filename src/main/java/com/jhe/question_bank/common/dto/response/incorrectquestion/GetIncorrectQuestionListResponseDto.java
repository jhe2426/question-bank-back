package com.jhe.question_bank.common.dto.response.incorrectquestion;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.vo.QuestionVO;

import lombok.Getter;

@Getter
public class GetIncorrectQuestionListResponseDto extends ResponseDto {
    private List<QuestionVO> questions;

    private GetIncorrectQuestionListResponseDto(List<QuestionEntity> questionEntities) {
        this.questions = QuestionVO.getQuestionList(questionEntities);
    }

    public static ResponseEntity<GetIncorrectQuestionListResponseDto> success(List<QuestionEntity> questionEntities) {
        GetIncorrectQuestionListResponseDto body = new GetIncorrectQuestionListResponseDto(questionEntities);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
