package com.example.repository;

import com.example.entity.RegionEntity;
import com.example.mapper.RegionLanguageMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends CrudRepository<RegionEntity, Integer>,
        PagingAndSortingRepository<RegionEntity,Integer> {

    Optional<RegionEntity> findByNameEngOrNameUzOrNameRu(String engName, String ruName, String uzName);
    Optional<RegionEntity>findByOrderNumber(Integer orderNum);
    @Transactional
    @Modifying
    @Query("update RegionEntity set visible=false where id=:id")
    int deleteRegionById(@Param("id") Integer id);
    @Query("select new com.example.mapper.RegionLanguageMapper(r.id, r.orderNumber, r.nameEng) from RegionEntity as r where r.visible=true")
    List<RegionLanguageMapper> getByEnglish();
    @Query("select new com.example.mapper.RegionLanguageMapper(r.id, r.orderNumber, r.nameUz) from RegionEntity as r where r.visible=true")
    List<RegionLanguageMapper> getByUz();
    @Query("select new com.example.mapper.RegionLanguageMapper(r.id, r.orderNumber, r.nameRu)from RegionEntity as r where r.visible=true")
    List<RegionLanguageMapper> getByRussian();
}
