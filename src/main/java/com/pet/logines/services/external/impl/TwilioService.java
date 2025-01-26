package com.pet.logines.services.external.impl;

import com.pet.logines.services.external.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import com.pet.logines.configurations.TwilioConfig;
import org.springframework.stereotype.Service;

@Service
public class TwilioService implements SmsService {

    @Override
    public void sendSms(String to, String msgToSend) {
        Message message = Message.creator(
                new PhoneNumber(to),            // Số điện thoại nhận
                new PhoneNumber(TwilioConfig.TWILIO_PHONE_NUMBER), // Số Twilio
                msgToSend     // Nội dung tin nhắn
        ).create();

        System.out.println("Message sent: " + message.getSid());
    }
}
