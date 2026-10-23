package com.jhe.question_bank.common.dto.request.killer.exam;

import java.util.List;

import com.jhe.question_bank.common.vo.UserAnswerVO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostKillerExamGradingRequestDto {
    @NotNull
    private Integer sessionId;
    @NotNull
    private Integer timeSpentSeconds;
    @NotNull
    private List<UserAnswerVO> userAnswers;
}
