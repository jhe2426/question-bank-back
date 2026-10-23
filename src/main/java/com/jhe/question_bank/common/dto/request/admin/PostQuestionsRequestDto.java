package com.jhe.question_bank.common.dto.request.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
@NoArgsConstructor
public class PostQuestionsRequestDto {

    @NotEmpty
    private List<@Valid AdminQuestionDto> questions;

}
