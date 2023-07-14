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
    Optional<CategoryEntity>findByNameEngOrNameUzOrNameRu(String nameEng, String nameUz, String nameRu);
    Optional<CategoryEntity>findByOrderNumber(Integer orderNumber);
    @Transactional
    @Modifying
    @Query("update CategoryEntity set visible=false where id=:id")
    int deleteCategory(@Param("id") Integer id);
    @Query("select new com.example.mapper.CategoryLanguageMapper(id, orderNumber,nameEng) from CategoryEntity where visible=true")
    List<CategoryLanguageMapper>getByEnglish();
    @Query("select new com.example.mapper.CategoryLanguageMapper(id, orderNumber,nameUz) from CategoryEntity where visible=true")
    List<CategoryLanguageMapper>getByUzbek();
    @Query("select new com.example.mapper.CategoryLanguageMapper(id, orderNumber,nameRu) from CategoryEntity where visible=true")
    List<CategoryLanguageMapper>getByRussian();


}
