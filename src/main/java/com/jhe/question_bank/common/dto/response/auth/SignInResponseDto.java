package com.jhe.question_bank.common.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jhe.question_bank.common.dto.response.ResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponseDto extends ResponseDto{
    private String accessToken;
    private Integer expiration;
    @JsonIgnore
    private String refreshToken;
    @JsonIgnore
    private String csrfToken;

    private SignInResponseDto(String accessToken, String refreshToken, String csrfToken) {
        this.accessToken = accessToken;
        this.expiration = 60 * 5;
        this.refreshToken = refreshToken;
        this.csrfToken = csrfToken;
    }

    public static ResponseEntity<SignInResponseDto> success(String accessToken, String refreshToken, String csrfToken) {
        SignInResponseDto body = new SignInResponseDto(accessToken, refreshToken, csrfToken);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
