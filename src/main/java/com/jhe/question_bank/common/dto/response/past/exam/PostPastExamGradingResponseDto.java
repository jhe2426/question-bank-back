package com.jhe.question_bank.common.dto.response.past.exam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class PostPastExamGradingResponseDto extends ResponseDto{
    private Integer groupId;

    private PostPastExamGradingResponseDto (Integer groupId) {
        this.groupId = groupId;
    }

    public static ResponseEntity<PostPastExamGradingResponseDto> success (Integer groupId) {
        PostPastExamGradingResponseDto body = new PostPastExamGradingResponseDto(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
