package com.jhe.question_bank.common.dto.request.mypage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchUserPasswordRequestDto {
    @NotBlank
    private String currentPassword;
    @NotBlank
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*-_?])[a-zA-Z0-9!@#$%^&*-_?]{8,20}$")
    private String newPassword;
}
