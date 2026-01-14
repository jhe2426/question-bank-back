package com.jhe.question_bank.common.dto.response;

public interface ResponseMessage {
    String SUCCESS = "Success.";

    String VALIDATION_FAIL = "Validation failed.";

    String AUTHENTICATION_FAIL = "Authentication failed.";
    String SIGN_IN_FAIL = "Sign in Fail.";

    String DATABASE_ERROR = "Database error.";
    String REFRESH_TOKEN_OPERATION_FAIL = "Refresh token operation failed.";
}
