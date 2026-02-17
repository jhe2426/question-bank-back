package com.jhe.question_bank.common.dto.response.past.exam;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.PastExamEntity;
import com.jhe.question_bank.common.vo.PastExamRoundVO;

import lombok.Getter;

@Getter
public class GetPastExamRoundsResponseDto extends ResponseDto{
    private List<PastExamRoundVO> pastExamRounds;

    private GetPastExamRoundsResponseDto(List<PastExamEntity> pastExamEntities) {
        this.pastExamRounds = PastExamRoundVO.getList(pastExamEntities);
    }

    public static ResponseEntity<GetPastExamRoundsResponseDto> success(List<PastExamEntity> pastExamEntities) {
        GetPastExamRoundsResponseDto body = new GetPastExamRoundsResponseDto(pastExamEntities);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
