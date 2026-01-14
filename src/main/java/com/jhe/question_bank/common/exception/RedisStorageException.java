package com.jhe.question_bank.common.exception;

import com.jhe.question_bank.common.dto.response.ResponseMessage;

public class RedisStorageException extends RuntimeException {
    public RedisStorageException() {
        super(ResponseMessage.REFRESH_TOKEN_OPERATION_FAIL);
    }
}
