package com.jhe.question_bank.common.dto.request.past.exam;

import java.util.List;

import com.jhe.question_bank.common.vo.PastExamUserAnswerVO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostPastExamGradingRequestDto {
    @NotBlank
    private String sourceType;
    @NotBlank
    private String detailedType;
    @NotNull
    private Integer pastExamId;
    @NotNull
    private List<PastExamUserAnswerVO> userAnswers;
}
