package com.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    void sendEmail(String toAccount, String subject, String text){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(toAccount);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendEmailMaim(String toAccount, String subject, String text){
        try {
            MimeMessage msg=javaMailSender.createMimeMessage();
//            msg.setFrom(fromEmail);
            MimeMessageHelper helper=new MimeMessageHelper(msg,true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
