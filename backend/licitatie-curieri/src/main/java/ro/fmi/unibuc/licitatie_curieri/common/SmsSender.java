package ro.fmi.unibuc.licitatie_curieri.common;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";
    public static final String TWILIO_NUMBER = "";

    public void sendSms(String to, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message sms = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(TWILIO_NUMBER),
                message
        ).create();
    }
}
