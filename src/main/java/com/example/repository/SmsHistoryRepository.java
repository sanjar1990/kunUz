package com.example.repository;

import com.example.entity.SmsHistoryEntity;
import com.example.enums.SmsStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity, String>,
        PagingAndSortingRepository<SmsHistoryEntity,String> {
    Optional<SmsHistoryEntity>findByPhoneAndMessageOrderByCreatedDateDesc(String phone,String message);
    @Transactional
    @Modifying
    @Query(value = "update SmsHistoryEntity set status=:status where phone=:phone")
    int updateSmsHistoryStatus(@Param("status")SmsStatus status,@Param("phone") String phone);
}
