package com.jhe.question_bank.common.dto.response.killer.exam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class PostKillerExamGradingResponseDto extends ResponseDto{
    private Integer groupId;

    private PostKillerExamGradingResponseDto (Integer groupId) {
        this.groupId = groupId;
    }

    public static ResponseEntity<PostKillerExamGradingResponseDto> success (Integer groupId) {
        PostKillerExamGradingResponseDto body = new PostKillerExamGradingResponseDto(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
