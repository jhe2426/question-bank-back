package com.jhe.question_bank.common.util;

import java.security.SecureRandom;

public class AuthCodeCreator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String APPROVAL_CHAR_SET = 
    "0123456789"  + 
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + 
    "abcdefghijklmnopqrstuvwxyz";
    
    public static String generatePhoneNumberAuthCode() {

        int codeLength = 4;
        StringBuilder authCode = new StringBuilder(codeLength);

        for (int count = 0; count < codeLength; count++)
            authCode.append(RANDOM.nextInt(10));

        return authCode.toString();
        
    }

    public static String generateApprovalCode() {

        int codeLength = 10;
        StringBuilder approvalCode = new StringBuilder(codeLength);
        
        
        for (int count = 0; count < codeLength; count++) {
            int index = RANDOM.nextInt(APPROVAL_CHAR_SET.length());
            approvalCode.append(APPROVAL_CHAR_SET.charAt(index));
        }
            
        return approvalCode.toString();
    }

}
