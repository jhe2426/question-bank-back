package com.jhe.question_bank.common.dto.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class ResponseDto {
    private String code;
    private String message;

    protected ResponseDto() {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<ResponseDto> success(HttpStatus status) {
        ResponseDto body = new ResponseDto();
        return ResponseEntity.status(status).body(body);
    }

    public static <T> ResponseEntity<T> successWithCookies(
        T body,
        String refreshCookie,
        String csrfCookie
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshCookie)
                .header(HttpHeaders.SET_COOKIE, csrfCookie)
                .body(body);
    }

    public static ResponseEntity<ResponseDto> validationFail() {
        ResponseDto body = new ResponseDto(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
  
    public static ResponseEntity<ResponseDto> existsUserId() {
        ResponseDto body = new ResponseDto(ResponseCode.EXISTS_USER_ID, ResponseMessage.EXISTS_USER_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    public static ResponseEntity<ResponseDto> existsUserPhoneNumber() {
        ResponseDto body = new ResponseDto(ResponseCode.EXISTS_USER_PHONE_NUMBER, ResponseMessage.EXISTS_USER_PHONE_NUMBER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    public static ResponseEntity<ResponseDto> authenticationFail() {
        ResponseDto body = new ResponseDto(ResponseCode.AUTHENTICATION_FAIL, ResponseMessage.AUTHENTICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    public static ResponseEntity<ResponseDto> signInFail() {
        ResponseDto body = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    public static ResponseEntity<ResponseDto> authorizationCodeExpired() {
        ResponseDto body = new ResponseDto(ResponseCode.AUTHORIZATION_CODE_EXPIRED, ResponseMessage.AUTHORIZATION_CODE_EXPIRED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    public static ResponseEntity<ResponseDto> authCodeAlreadySent() {
        ResponseDto body = new ResponseDto(ResponseCode.AUTH_CODE_ALREADY_SENT, ResponseMessage.AUTH_CODE_ALREADY_SENT);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(body);
    }

    public static ResponseEntity<ResponseDto> smsSendFail() {
        ResponseDto body = new ResponseDto(ResponseCode.SMS_SEND_FAILED, ResponseMessage.SMS_SEND_FAILED);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    public static ResponseEntity<ResponseDto> redisServerError() {
        ResponseDto body = new ResponseDto(ResponseCode.REDIS_SERVER_ERROR, ResponseMessage.REDIS_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto body = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
