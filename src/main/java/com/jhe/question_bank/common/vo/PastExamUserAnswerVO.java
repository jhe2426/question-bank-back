package com.jhe.question_bank.common.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PastExamUserAnswerVO {
    @NotNull
    private Integer questionId;
    @NotNull
    private Integer answer;
}
