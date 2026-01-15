package com.jhe.question_bank.common.dto.response;

public interface ResponseCode {
    String SUCCESS = "SU";

    String VALIDATION_FAIL = "VF";

    String AUTHENTICATION_FAIL = "AF";
    String SIGN_IN_FAIL = "SF";

    String SERVER_ERROR = "SE";
    String DATABASE_ERROR = "DBE";
    String REFRESH_TOKEN_OPERATION_FAIL = "ROF";
}
