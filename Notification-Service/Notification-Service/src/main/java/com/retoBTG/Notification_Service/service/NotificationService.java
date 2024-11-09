package com.retoBTG.Notification_Service.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);  // true para HTML

        emailSender.send(message);
    }

    public void sendSms(String to, String body) {
        // Configuración de Twilio
        String ACCOUNT_SID = "YOUR_TWILIO_ACCOUNT_SID";
        String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";
        String FROM_PHONE = "YOUR_TWILIO_PHONE_NUMBER";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(FROM_PHONE),
                        body)
                .create();
    }
}
