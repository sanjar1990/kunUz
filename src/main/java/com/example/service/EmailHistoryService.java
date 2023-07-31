package com.example.service;

import com.example.dto.EmailHistoryDTO;
import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    public void sendEmail(String message, ProfileEntity profile){
        EmailHistoryEntity entity= new EmailHistoryEntity();
        entity.setMessage(message);
        entity.setEmail(profile.getEmail());
        emailHistoryRepository.save(entity);
    }
    // 2. Get EmailHistory by email
    public List<EmailHistoryDTO> getEmailHistoryByEmail(String email){
        List<EmailHistoryEntity> entityList=emailHistoryRepository.findAllByEmail(email);
        return entityList.stream().map(this::toDto).toList();
    }
    //3. Get EmailHistory  by given date
    public List<EmailHistoryDTO>getByDate(LocalDate date){
        List<EmailHistoryEntity>entityList=emailHistoryRepository.findAllByCreatedDateBetween(date.atStartOfDay(), LocalDateTime.of(date, LocalTime.MAX));
        return entityList.stream().map(this::toDto).toList();
    }
    // 4. Pagination (ADMIN) (id, email,message,created_date)
    public PageImpl<EmailHistoryDTO>pagination(Integer page, Integer size){
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDate"));
        Page<EmailHistoryEntity> pageObj=emailHistoryRepository.findAll(pageable);
        List<EmailHistoryDTO>dtoList=pageObj.getContent().stream().map(s->toDto(s)).collect(Collectors.toList());
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    private EmailHistoryDTO toDto(EmailHistoryEntity entity){
        EmailHistoryDTO emailHistoryDTO=new EmailHistoryDTO();
        emailHistoryDTO.setEmail(entity.getEmail());
        emailHistoryDTO.setMessage(entity.getMessage());
        emailHistoryDTO.setCreateDate(entity.getCreatedDate());
        emailHistoryDTO.setId(entity.getId());
        return emailHistoryDTO;
    }


}
