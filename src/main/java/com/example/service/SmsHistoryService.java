package com.example.service;

import com.example.entity.SmsHistoryEntity;
import com.example.enums.SmsStatus;
import com.example.enums.SmsType;
import com.example.repository.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsHistoryService {
@Autowired
private SmsHistoryRepository smsHistoryRepository;
    public void save(String message, String phone) {
        SmsHistoryEntity entity=new SmsHistoryEntity();
        entity.setPhone(phone);
        entity.setMessage(message);
        entity.setStatus(SmsStatus.NEW);
        entity.setType(SmsType.REGISTRATION);
        smsHistoryRepository.save(entity);
    }
}
