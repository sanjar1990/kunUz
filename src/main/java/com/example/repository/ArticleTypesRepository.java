package com.example.repository;


import com.example.entity.ArticleTypesEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticleTypesRepository extends CrudRepository<ArticleTypesEntity, Integer> {
    @Query("select articleTypeId from ArticleTypesEntity where articleId=:articleId")
    List<Integer>getAllTypeId(@Param("articleId")String articleId);
   void deleteByArticleIdAndArticleTypeId(@Param("articleId") String articleId, @Param("typeId") Integer typeId);
    void deleteByArticleId(String articleId);
}
