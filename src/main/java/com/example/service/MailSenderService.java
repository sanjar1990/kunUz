package com.example.service;

import com.example.entity.ProfileEntity;
import com.example.utility.JWTUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Value("${server.url}")
    private String serverUrl;

    void sendEmail(String toAccount, String subject, String text){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(toAccount);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendEmailVerification(String toAccount, ProfileEntity entity, Integer id){
        String jwt= JWTUtil.encodeEmailJWT(id);
        String url=serverUrl+"/api/v1/auth/verification/email"+jwt;
            StringBuilder builder=new StringBuilder();
            builder.append( String.format("<h1>Hello %s</h1>",entity.getName()));
            builder.append(" <p>");
            builder.append(String.format("<a href=\"%s\">Click the link to verify your account! </a>",url));
            builder.append(" </p>");

            sendMimeEmail(toAccount,"Kun uz Verification link",builder.toString());
            emailHistoryService.sendEmail(builder.toString(),entity );
    }
    public void sendMimeEmail(String toAccount, String subject, String text){
        ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
        emailExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    MimeMessage msg=javaMailSender.createMimeMessage();
                    MimeMessageHelper helper=new MimeMessageHelper(msg,true);
                    helper.setTo(toAccount);
                    helper.setSubject(subject);
                    helper.setText("nimadur",text);
                    javaMailSender.send(msg);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        emailExecutor.shutdown();
    }
}
