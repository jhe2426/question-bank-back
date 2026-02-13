package com.jhe.question_bank.common.dto.response.past.exam;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.PastExamQuestionEntity;
import com.jhe.question_bank.common.vo.PastExamRoundVO;

import lombok.Getter;

@Getter
public class GetPastExamRoundsResponseDto extends ResponseDto{
    private List<PastExamRoundVO> pastExamRounds;

    private GetPastExamRoundsResponseDto(List<PastExamQuestionEntity> pastExamQuestionEntities) {
        this.pastExamRounds = PastExamRoundVO.getList(pastExamQuestionEntities);
    }

    public static ResponseEntity<GetPastExamRoundsResponseDto> success(List<PastExamQuestionEntity> pastExamQuestionEntities) {
        GetPastExamRoundsResponseDto body = new GetPastExamRoundsResponseDto(pastExamQuestionEntities);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
