package com.jhe.question_bank.common.dto.response.killer.exam;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.vo.QuestionVO;

import lombok.Getter;

@Getter
public class GetKillerExamQuestionListResponseDto extends ResponseDto {
    private List<QuestionVO> questions;
    private Integer currentKillerExamRound;

    private GetKillerExamQuestionListResponseDto(List<QuestionEntity> questionEntities, Integer currentKillerExamRound) {
        this.questions = QuestionVO.getKillerExamQuestionList(questionEntities);
        this.currentKillerExamRound = currentKillerExamRound;
    }

    public static ResponseEntity<GetKillerExamQuestionListResponseDto> success (List<QuestionEntity> questionEntities, Integer currentKillerExamRound) {
        GetKillerExamQuestionListResponseDto body = new GetKillerExamQuestionListResponseDto(questionEntities, currentKillerExamRound);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
