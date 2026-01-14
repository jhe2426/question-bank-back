package com.jhe.question_bank.common.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jhe.question_bank.common.dto.response.ResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenRefreshResponseDto extends ResponseDto {
    private String accessToken;
    private Integer expiration;

        private AccessTokenRefreshResponseDto(String accessToken) {
        this.accessToken = accessToken;
        this.expiration = 60 * 5;
    }

    public static ResponseEntity<AccessTokenRefreshResponseDto> success(String accessToken) {
        AccessTokenRefreshResponseDto body = new AccessTokenRefreshResponseDto(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
