package com.example.repository;

import com.example.dto.ProfileDTO;
import com.example.entity.AttachEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ProfileStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer>,
        PagingAndSortingRepository<ProfileEntity,Integer> {

    Boolean existsAllByPhoneAndVisibleTrueAndStatus(String phone, ProfileStatus profileStatus);
    Boolean existsAllByEmailAndVisibleTrueAndStatus(String email,ProfileStatus profileStatus);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible=false where id=:id")
    int deleteVisible(@Param("id")Integer id);
    Optional<ProfileEntity> findByIdAndVisibleTrue(Integer profileId);
    Page<ProfileEntity>findAllByVisibleTrue(Pageable pageable);
    Optional<ProfileEntity>findAllByPhoneAndPasswordAndVisibleTrue(String phone, String password);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set photoId=:photoId where id=:profileId and visible=true")
    int updatePhoto(Integer profileId, String photoId);
    Optional<ProfileEntity>findByEmailAndVisibleTrue(String email);
}
