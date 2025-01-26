package com.pet.logines.configurations;

import com.twilio.Twilio;
import org.springframework.stereotype.Component;

@Component
public class TwilioConfig {
    public static final String TWILIO_PHONE_NUMBER = System.getenv("TWILIO_PHONE_NUMBER");

    public TwilioConfig() {
        String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
        String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
}
