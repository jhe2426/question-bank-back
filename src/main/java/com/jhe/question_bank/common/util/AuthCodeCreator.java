package com.jhe.question_bank.common.util;

import java.util.Random;

public class AuthCodeCreator {
    
    public static String generateNumber() {

        String authCode = "";

        Random random = new Random();
        for (int count = 0; count < 4; count++)
            authCode += random.nextInt(10);

        return authCode;
        
    }

}
