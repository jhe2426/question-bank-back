package com.jhe.question_bank.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.exception.CsrfException;
import com.jhe.question_bank.common.exception.RedisStorageException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception) {
        exception.printStackTrace();
        return ResponseDto.validationFail();
    }
    
    @ExceptionHandler(CsrfException.class)
    public ResponseEntity<ResponseDto> csrfExceptionHandler(CsrfException exception) {
        exception.printStackTrace();
        return ResponseDto.authenticationFail();
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ResponseDto> cookieValidationExceptionHandler(MissingRequestCookieException exception) {
        exception.printStackTrace();
        return ResponseDto.authenticationFail();
    }

    @ExceptionHandler(RedisStorageException.class)
    public ResponseEntity<ResponseDto> redisStorageExceptionHandler(RedisStorageException exception) {
        exception.printStackTrace();
        return ResponseDto.refreshTokenFail();
    }
}
