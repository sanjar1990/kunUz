package com.example.repository;

import com.example.dto.ProfileDTO;
import com.example.entity.ProfileEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer>,
        PagingAndSortingRepository<ProfileEntity,Integer> {

     Optional<ProfileEntity> findAllByPhone(String phone);
    Optional<ProfileEntity> findAllByEmail(String email);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible=false where id=:id")
    int deleteVisible(@Param("id")Integer id);

}
