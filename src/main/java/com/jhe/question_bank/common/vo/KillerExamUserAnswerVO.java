package com.jhe.question_bank.common.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class KillerExamUserAnswerVO {
    @NotNull
    private Integer questionId;
    @NotNull
    private Integer questionOrder;
    @NotNull
    private Integer answer;
}
