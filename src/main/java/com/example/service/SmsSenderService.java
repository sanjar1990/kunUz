package com.example.service;

import com.example.utility.RandomUtil;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderService {
    public void SendRegistrationSms(String phone){
        String smsCode= RandomUtil.getRandomSmsCode();
        String text="Kun uz tes ro'yxatdan o'tish ko'di:"+smsCode;
        //sendMessage(phone, text,SmsType.Registration, smsCode)
    }

}
