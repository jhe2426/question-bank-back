package com.jhe.question_bank.common.dto.response.mypage;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.entity.UserProblemGroupEntity;
import com.jhe.question_bank.common.vo.LearningRecordVO;

import lombok.Getter;

@Getter
public class GetLearningRecordListResponseDto extends ResponseDto{
    private List<LearningRecordVO> learningRecordes;

    private GetLearningRecordListResponseDto(List<UserProblemGroupEntity> userProblemGroupEntities) {
        this.learningRecordes = LearningRecordVO.getList(userProblemGroupEntities);
    }

    public static ResponseEntity<GetLearningRecordListResponseDto> success(List<UserProblemGroupEntity> userProblemGroupEntities) {
        GetLearningRecordListResponseDto body = new GetLearningRecordListResponseDto(userProblemGroupEntities);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

}
