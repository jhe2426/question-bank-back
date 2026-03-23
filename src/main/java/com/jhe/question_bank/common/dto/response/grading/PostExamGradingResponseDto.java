package com.jhe.question_bank.common.dto.response.grading;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class PostExamGradingResponseDto extends ResponseDto {
    private Integer groupId;

    private PostExamGradingResponseDto (Integer groupId) {
        this.groupId = groupId;
    }

    public static ResponseEntity<PostExamGradingResponseDto> success (Integer groupId) {
        PostExamGradingResponseDto body = new PostExamGradingResponseDto(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
