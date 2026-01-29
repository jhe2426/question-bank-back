package com.jhe.question_bank.common.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhoneNumberAuthCodeRequestDto {
    @NotBlank
    @Pattern(regexp="^[0-9]{11}$")
    private String phoneNumber;
}
