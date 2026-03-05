package com.jhe.question_bank.common.dto.response.past.exam;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.PastExamQuestionEntity;
import com.jhe.question_bank.common.entity.QuestionEntity;
import com.jhe.question_bank.common.vo.QuestionVO;

import lombok.Getter;

@Getter
public class GetPastExamQuestionListResponseDto extends ResponseDto {
    private List<QuestionVO> questions;

    private GetPastExamQuestionListResponseDto(List<QuestionEntity> questionEntities, Map<Integer, PastExamQuestionEntity> pastExamMap) {
        this.questions = QuestionVO.getPastExamQuestionList(questionEntities, pastExamMap);
    }

    public static ResponseEntity<GetPastExamQuestionListResponseDto> success(List<QuestionEntity> questionEntities, Map<Integer, PastExamQuestionEntity> pastExMap) {
        GetPastExamQuestionListResponseDto body = new GetPastExamQuestionListResponseDto(questionEntities, pastExMap);
        return  ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
