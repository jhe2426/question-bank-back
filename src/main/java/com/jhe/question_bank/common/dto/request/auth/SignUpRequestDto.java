package com.jhe.question_bank.common.dto.request.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank
    @Pattern(regexp="^[a-z0-9]{3,15}$")
    private String userId;

    @NotBlank
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*-_?])[a-zA-Z0-9!@#$%^&*-_?]{8,20}$")
    private String password;

    @NotBlank
    @Length(min=10, max=10)
    private String approvalCode;

    @NotBlank
    @Pattern(regexp="^010\\d{4}\\d{4}$")
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp="^[가-힣]{2,6}$")
    private String name;

    @NotBlank
    @Pattern(regexp="^[0-9]{8}$")
    private String birthDate;
    
    @NotBlank
    private String address;

    @NotNull
    private Integer universityId;

    @NotBlank
    @Pattern(regexp="^(여성|남성)$")
    private String gender; 
}
