package com.jhe.question_bank.common.dto.response;

public interface ResponseMessage {
    String SUCCESS = "Success.";

    String VALIDATION_FAIL = "Validation failed.";
    String EXISTS_USER_ID = "User id already exists.";
    String EXISTS_USER_PHONE_NUMBER = "User phone number already exists.";

    String AUTHENTICATION_FAIL = "Authentication failed.";
    String SIGN_IN_FAIL = "Sign in Fail.";
    String AUTHORIZATION_CODE_EXPIRED = "Authorization code has expired.";

    String AUTH_CODE_ALREADY_SENT = "Authentication code already sent. Please try again later.";

    String SMS_SEND_FAILED = "Failed to send verification code via SMS";
    String REDIS_SERVER_ERROR = "Redis server error occurred.";
    String DATABASE_ERROR = "Database error.";
}
