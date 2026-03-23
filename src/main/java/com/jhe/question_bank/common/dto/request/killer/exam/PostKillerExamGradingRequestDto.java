package com.jhe.question_bank.common.dto.request.killer.exam;

import java.util.List;

import com.jhe.question_bank.common.vo.KillerExamUserAnswerVO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostKillerExamGradingRequestDto {
    @NotBlank
    private String sourceType;
    @NotNull
    private Integer sessionId;
    @NotNull
    private List<KillerExamUserAnswerVO> userAnswers;
}
