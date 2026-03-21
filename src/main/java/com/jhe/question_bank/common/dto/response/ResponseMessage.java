package com.jhe.question_bank.common.dto.response;

public interface ResponseMessage {
    String SUCCESS = "Success.";

    String VALIDATION_FAILED = "Validation failed.";
    String EXISTS_USER_ID = "User id already exists.";
    String EXISTS_USER_PHONE_NUMBER = "User phone number already exists.";
    String USED_APPROVAL_CODE = "Approval code already used.";
    String PHONE_NUMBER_AUTH_FAILED = "Phone number authentication failed.";
    String APPROVAL_CODE_AUTH_FAILED = "Approval code authentication failed.";
    String PAST_EXAM_QUESTION_ID_NOT_FOUND = "The past exam question ID does not exist.";
    String QUESTION_ID_NOT_FOUND = "Question ID does not exist.";
    String NO_REMAINING_MOCK_EXAM_QUESTIONS = "No remaining questions for the selected difficulty and chapter.";

    String AUTHENTICATION_FAIL = "Authentication failed.";
    String SIGN_IN_FAIL = "Sign in Fail.";
    String AUTHORIZATION_CODE_EXPIRED = "Authorization code has expired.";
    String NOT_EXIST_USER_ID = "User ID does not exist.";

    String AUTH_CODE_ALREADY_SENT = "Authentication code already sent. Please try again later.";

    String SMS_SEND_FAILED = "Failed to send verification code via SMS";
    String REDIS_SERVER_ERROR = "Redis server error occurred.";
    String DATABASE_ERROR = "Database error.";
}
