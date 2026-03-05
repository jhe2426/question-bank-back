package com.jhe.question_bank.common.dto.response;

public interface ResponseCode {
    String SUCCESS = "SU";

    String VALIDATION_FAIL = "VF";
    String EXISTS_USER_ID = "EUI";
    String EXISTS_USER_PHONE_NUMBER = "EUP";
    String USED_APPROVAL_CODE = "AU";
    String PHONE_NUMBER_AUTH_FAILED = "PAF";
    String APPROVAL_CODE_AUTH_FAILED = "AAF";
    String PAST_EXAM_QUESTION_ID_NOT_FOUND = "NPE";

    String AUTHENTICATION_FAIL = "AF";
    String SIGN_IN_FAIL = "SF";
    String AUTHORIZATION_CODE_EXPIRED = "AE";

    String AUTH_CODE_ALREADY_SENT = "AR";

    String SMS_SEND_FAILED = "SSF";
    String REDIS_SERVER_ERROR = "RE";
    String DATABASE_ERROR = "DBE";
}
