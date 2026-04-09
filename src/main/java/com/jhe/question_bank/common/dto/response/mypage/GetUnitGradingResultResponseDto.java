package com.jhe.question_bank.common.dto.response.mypage;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.vo.UnitGradingResultVO;
import com.jhe.question_bank.repository.resultSet.GetUnitGradingResultSet;

import lombok.Getter;

@Getter
public class GetUnitGradingResultResponseDto extends ResponseDto{
    private List<UnitGradingResultVO> unitGradingResults;

    private GetUnitGradingResultResponseDto(List<GetUnitGradingResultSet> resultSets) {
        this.unitGradingResults = UnitGradingResultVO.getList(resultSets);
    }

    public static ResponseEntity<GetUnitGradingResultResponseDto> success(List<GetUnitGradingResultSet> resultSets) {
        GetUnitGradingResultResponseDto body = new GetUnitGradingResultResponseDto(resultSets);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    
}
