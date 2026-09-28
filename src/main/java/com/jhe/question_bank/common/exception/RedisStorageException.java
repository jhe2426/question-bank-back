package com.jhe.question_bank.common.exception;

import com.jhe.question_bank.common.dto.response.ResponseMessage;

public class RedisStorageException extends RuntimeException {
    public RedisStorageException(Throwable cause) {
        super(ResponseMessage.REDIS_SERVER_ERROR, cause);
    }
}
