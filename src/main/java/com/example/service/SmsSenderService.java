package com.example.service;

import com.example.dto.RegionDTO;
import com.example.dto.SmsDTO;
import com.example.entity.ProfileEntity;
import com.example.exception.AppBadRequestException;
import com.example.utility.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Service
public class SmsSenderService {
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Value("${smsProvider.url}")
    private String smsProviderUrl;
    @Value("${smsSend.url}")
    private String smsSendUrl;

    @Value("${smsProvider.login}")
    private String login;
    @Value("${smsProvider.password}")
    private String password;
    private LocalDateTime tokenTime=null;
    private  String token=null;
    public SmsDTO SendRegistrationSms(ProfileEntity entity){
        if (tokenTime==null || tokenTime.plusDays(7).isAfter(LocalDateTime.now())){
            loginToSmsProvider();
        }
        System.out.println(token);
        String smsCode= RandomUtil.getRandomSmsCode();
       return sendMessage(entity.getPhone(), smsCode);
    }
    private void loginToSmsProvider(){
    RestTemplate restTemplate=new RestTemplate();
        String response = restTemplate.getForObject(smsProviderUrl+"?login={this.login}&password={this.password}",
                String.class, login, password);
        System.out.println(response);
        token=response;
        tokenTime=LocalDateTime.now();
    }
    private SmsDTO sendMessage(String phone, String code){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jwt="Bearer "+token;
        headers.set("Authorization",jwt);
        SmsDTO smsDTO=new SmsDTO();
        smsDTO.setPhone(phone);
        smsDTO.setMessage("Kun uz tes ro'yxatdan o'tish ko'di:  "+code);
        String json;
        try {
            json=new ObjectMapper().writeValueAsString(smsDTO);
        } catch (JsonProcessingException e) {
            System.out.println("exception");
            throw new AppBadRequestException(e.getMessage());
        }
        System.out.println("Json:"+ json);
        HttpEntity entity=new HttpEntity(json,headers);
            RestTemplate restTemplate= new RestTemplate();
            SmsDTO response= restTemplate.exchange(smsSendUrl, HttpMethod.POST,entity,SmsDTO.class).getBody();
            if (response!=null){
                smsHistoryService.save(code,response.getPhone());
            }
            return response;
    }
}
