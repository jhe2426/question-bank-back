package com.jhe.question_bank.common.dto.response.examresult;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.vo.ExamResultVO;
import com.jhe.question_bank.repository.resultSet.GetExamResultSet;

import lombok.Getter;

@Getter
public class GetExamResultResponseDto extends ResponseDto {
    private String sourceType;
    private String detailedType;
    private List<ExamResultVO> examResults;
    
    private GetExamResultResponseDto(List<GetExamResultSet> examResultSets, String sourceType, String detailedType) {
        this.sourceType = sourceType;
        this.detailedType = detailedType;
        this.examResults = ExamResultVO.getList(examResultSets);
    }

    public static ResponseEntity<GetExamResultResponseDto> success(List<GetExamResultSet> examResultSets, String sourceType, String detailedType) {
        GetExamResultResponseDto body = new GetExamResultResponseDto(examResultSets, sourceType, detailedType);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
