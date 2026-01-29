package com.jhe.question_bank.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;

@Component
public class SmsProvider {
    
    private final DefaultMessageService messageService;
    private final String from;

    public SmsProvider(
        @Value("${solapi-sms.api-key}") String apiKey,
        @Value("${solapi-sms.secret-key}") String apiSecretKey,
        @Value("${solapi-sms.from}") String from
    ) {
        this.messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecretKey);
        this.from = from;
    }

    public boolean sendPhoneNumberAuthCodeMessage(String to, String authCode) {

        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("Adsp 문제은행 인증번호은 [" + authCode + "]입니다. \n5분 이내에 입력해주세요");

        try {
            messageService.send(message);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean sendApprovalCodeMessage(String to, String approvalCode) {

        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("Adsp 문제은행의 승인번호는 [" + approvalCode + "]입니다.");

        try {
            messageService.send(message);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

}
