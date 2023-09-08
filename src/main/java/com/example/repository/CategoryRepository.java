package com.example.repository;

import com.example.entity.CategoryEntity;
import com.example.entity.RegionEntity;
import com.example.mapper.CategoryLanguageMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer>,
        PagingAndSortingRepository<CategoryEntity,Integer> {
    Boolean existsAllByNameEnOrNameUzOrNameRuOrOrderNumber(String nameEn, String nameUz, String nameRu,Integer orderNumber);
    Optional<CategoryEntity>findByOrderNumber(Integer orderNumber);
    @Transactional
    @Modifying
    @Query("update CategoryEntity set visible=false where id=:id")
    int deleteCategory(@Param("id") Integer id);
    Optional<CategoryEntity> findByIdAndVisibleTrue(Integer id);
    List<CategoryEntity>findAllByVisibleTrueOrderByCreatedDateDesc();

    @Query(value = "select id, order_number as orderNumber, " +
            "(case :lang" +
            " when 'uz' then name_uz" +
            " when 'ru' then name_ru" +
            " when 'en' then name_en" +
            " else name_uz" +
            " end) as name" +
            " from category where visible=true" +
            " order by order_number asc ", nativeQuery = true)
    List<CategoryLanguageMapper>getAllByLang(@Param("lang") String lang);
    Optional<CategoryEntity>findByNameUz(String nameUz);



}
