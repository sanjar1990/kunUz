package com.example.repository;

import com.example.entity.RegionEntity;
import com.example.enums.Language;
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

   Boolean existsAllByNameEnOrNameUzOrNameRu(String enName, String ruName, String uzName);
    Optional<RegionEntity>findByOrderNumber(Integer orderNum);
    @Transactional
    @Modifying
    @Query("update RegionEntity set visible=false where id=:id")
    int deleteRegionById(@Param("id") Integer id);
    Optional<RegionEntity>findByIdAndVisibleTrue(Integer id);
    List<RegionEntity>findAllByVisibleTrueOrderByOrderNumberAsc();

    @Query(value = "select id, order_number as orderNumber," +
            " (CASE :lang" +
            " WHEN 'uz' THEN name_uz" +
            " WHEN 'en' THEN name_en" +
            " when 'ru' then name_ru " +
            "ELSE name_uz" +
            " end) as name " +
            " from region where visible=true order by order_number", nativeQuery = true)
 List<RegionLanguageMapper> getByLanguage(@Param("lang") String language);
}
