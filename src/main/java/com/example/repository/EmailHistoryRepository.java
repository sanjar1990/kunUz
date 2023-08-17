package com.example.repository;


import com.example.entity.EmailHistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, String>,
        PagingAndSortingRepository<EmailHistoryEntity,String> {
//     2. Get EmailHistory by email
    List<EmailHistoryEntity> findAllByEmail(String email);
    List<EmailHistoryEntity>findAllByCreatedDateBetween(LocalDateTime from, LocalDateTime to);
}
