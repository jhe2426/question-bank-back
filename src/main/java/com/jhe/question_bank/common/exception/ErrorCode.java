package com.jhe.question_bank.common.exception;

import org.springframework.http.HttpStatus;

import com.jhe.question_bank.common.dto.response.ResponseCode;
import com.jhe.question_bank.common.dto.response.ResponseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_FAIL(
        HttpStatus.BAD_REQUEST,
        ResponseCode.VALIDATION_FAIL, 
        ResponseMessage.VALIDATION_FAILED
    ),

    EXISTS_USER_ID(
        HttpStatus.BAD_REQUEST,
        ResponseCode.EXISTS_USER_ID, 
        ResponseMessage.EXISTS_USER_ID
    ),

    EXISTS_USER_PHONE_NUMBER(
        HttpStatus.BAD_REQUEST,
        ResponseCode.EXISTS_USER_PHONE_NUMBER, 
        ResponseMessage.EXISTS_USER_PHONE_NUMBER
    ),

    USED_APPROVAL_CODE(
        HttpStatus.BAD_REQUEST,
        ResponseCode.USED_APPROVAL_CODE, 
        ResponseMessage.USED_APPROVAL_CODE
    ),

    PHONE_NUMBER_AUTH_FAILED(
        HttpStatus.BAD_REQUEST,
        ResponseCode.PHONE_NUMBER_AUTH_FAILED, 
        ResponseMessage.PHONE_NUMBER_AUTH_FAILED
    ),
        
    APPROVAL_CODE_AUTH_FAILED(
        HttpStatus.BAD_REQUEST,
        ResponseCode.APPROVAL_CODE_AUTH_FAILED, 
        ResponseMessage.APPROVAL_CODE_AUTH_FAILED
    ),

    PAST_EXAM_QUESTION_ID_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        ResponseCode.PAST_EXAM_QUESTION_ID_NOT_FOUND,
        ResponseMessage.PAST_EXAM_QUESTION_ID_NOT_FOUND
    ),

    QUESTION_ID_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        ResponseCode.QUESTION_ID_NOT_FOUND, 
        ResponseMessage.QUESTION_ID_NOT_FOUND
    ),

    INCORRECT_QUESTION_NOT_EXISTS(
        HttpStatus.BAD_REQUEST,
        ResponseCode.INCORRECT_QUESTION_NOT_EXISTS, 
        ResponseMessage.INCORRECT_QUESTION_NOT_EXISTS
    ),

    NOT_EXISTS_QUESTION_GROUP(
        HttpStatus.BAD_REQUEST,
        ResponseCode.NOT_EXISTS_QUESTION_GROUP, 
        ResponseMessage.NOT_EXISTS_QUESTION_GROUP
    ),

    PASSWORD_INCOREECT(
        HttpStatus.BAD_REQUEST,
        ResponseCode.PASSWORD_INCOREECT, 
        ResponseMessage.PASSWORD_INCOREECT
    ),

    NOT_PAST_EXAM_GROUP(
        HttpStatus.BAD_REQUEST,
        ResponseCode.NOT_PAST_EXAM_GROUP, 
        ResponseMessage.NOT_PAST_EXAM_GROUP
    ),

    NEW_PASSWORD_SAME_AS_CURRENT(
        HttpStatus.BAD_REQUEST,
        ResponseCode.NEW_PASSWORD_SAME_AS_CURRENT, 
        ResponseMessage.NEW_PASSWORD_SAME_AS_CURRENT
    ),

    AUTHENTICATION_FAIL(
        HttpStatus.UNAUTHORIZED,
        ResponseCode.AUTHENTICATION_FAIL, 
        ResponseMessage.AUTHENTICATION_FAIL
    ),

    SIGN_IN_FAIL(
        HttpStatus.UNAUTHORIZED,
        ResponseCode.SIGN_IN_FAIL, 
        ResponseMessage.SIGN_IN_FAIL
    ),

    AUTHORIZATION_CODE_EXPIRED(
        HttpStatus.UNAUTHORIZED,
        ResponseCode.AUTHORIZATION_CODE_EXPIRED, 
        ResponseMessage.AUTHORIZATION_CODE_EXPIRED
    ),

    NOT_EXIST_USER_ID(
        HttpStatus.UNAUTHORIZED,
        ResponseCode.NOT_EXIST_USER_ID, 
        ResponseMessage.NOT_EXIST_USER_ID
    ),


    NO_PERMISSION(
        HttpStatus.FORBIDDEN,
        ResponseCode.NO_PERMISSION, 
        ResponseMessage.NO_PERMISSION
    ),

    AUTH_CODE_ALREADY_SENT(
        HttpStatus.TOO_MANY_REQUESTS,
        ResponseCode.AUTH_CODE_ALREADY_SENT, 
        ResponseMessage.AUTH_CODE_ALREADY_SENT
    ),

    SMS_SEND_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ResponseCode.SMS_SEND_FAILED,
         ResponseMessage.SMS_SEND_FAILED
    ),

    REDIS_SERVER_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ResponseCode.REDIS_SERVER_ERROR, 
        ResponseMessage.REDIS_SERVER_ERROR
    ),

    DATABASE_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ResponseCode.DATABASE_ERROR, 
        ResponseMessage.DATABASE_ERROR
    );

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
