package com.jhe.question_bank.common.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalCodeVerifyRequestDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String approvalCode;
}
