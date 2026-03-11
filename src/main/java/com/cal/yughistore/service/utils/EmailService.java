package com.cal.yughistore.service.utils;

import com.resend.*;
import com.resend.services.emails.model.CreateEmailOptions;
import com.cal.yughistore.model.EmailMessage;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private String key;

    public EmailService(Dotenv dotenv) {
        this.key = dotenv.get("RESEND_KEY");
    }

    public void sendEmail(EmailMessage email) {
        Resend resend = new Resend(key);
        CreateEmailOptions createEmailOptions = CreateEmailOptions.builder()
                .from("send@veemdigital.com")
                .to(email.getTo())
                .subject(email.getSubject())
                .html(email.getBody())
                .build();
//TODO : send email using resend
//        try {
//            CreateEmailResponse data = resend.emails().send(createEmailOptions);
//            logger.info("Email created and sent to={} subject={}", email.getTo(),
//                    email.getSubject());
//        } catch (ResendException e) {
//            logger.error("Send email failed while sending to={} with subject={}",
//                    email.getTo(), email.getSubject(), e);
//        }
    }
}
