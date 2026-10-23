package com.jhe.question_bank.common.dto.request.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminQuestionDto {
    @NotNull
    private Integer sectionId;

    @NotBlank
    private String questionType;

    private String difficulty;

    @NotBlank
    @Size(max = 255)
    private String questionText;

    private String imageFileName;

    @NotBlank
    @Size(max = 255)
    private String option1;

    @NotBlank
    @Size(max = 255)
    private String option2;

    @NotBlank
    @Size(max = 255)
    private String option3;

    @NotBlank
    @Size(max = 255)
    private String option4;

    @NotNull
    @Min(1)
    @Max(4)
    private Integer answer;

    private String shortExplanation;

    @Valid
    private DetailedExplanationDto detailedExplanation;

    // 모의고사 기출 회차 태그: "35,36,37"
    private String tags;

    // 기출문제인 경우에만 사용
    private Integer pastExamId;
    private Integer questionOrder;
}
