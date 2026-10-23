package com.jhe.question_bank.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jhe.question_bank.common.dto.response.ResponseDto;
import com.jhe.question_bank.common.exception.BusinessException;
import com.jhe.question_bank.common.exception.CsrfException;
import com.jhe.question_bank.common.exception.RedisStorageException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BusinessException.class) 
    public ResponseEntity<ResponseDto> businessExceptionHandler(BusinessException exception) {
        return ResponseDto.error(exception.getErrorCode());
    }

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
        return ResponseDto.redisServerError();
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseDto> databaseExceptionHandler(DataAccessException exception) {
        return ResponseDto.databaseError();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> exceptionHandler(Exception exception) {
        return ResponseDto.internalServerError();
    }
}
