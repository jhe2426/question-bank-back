package com.jhe.question_bank.common.exception;

import com.jhe.question_bank.common.dto.response.ResponseMessage;

public class CsrfException extends RuntimeException {
    public CsrfException() {
        super(ResponseMessage.AUTHENTICATION_FAIL);
    }
    
}
