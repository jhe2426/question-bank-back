package com.jhe.question_bank.common.dto.request.incorrectquestions;

import java.util.List;

import com.jhe.question_bank.common.vo.UserAnswerVO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostIncorrectQuestionGradingRequestDto {
    @NotBlank
    private String sourceType;
    @NotBlank
    private String detailedType;
    @NotNull
    private List<UserAnswerVO> userAnswers;
}
