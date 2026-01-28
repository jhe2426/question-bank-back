package com.jhe.question_bank.common.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IdCheckRequestDto {
    @NotBlank
    @Pattern(regexp="^[a-z0-9]{3,15}$")
    private String userId;
}
