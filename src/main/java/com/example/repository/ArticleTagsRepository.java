package com.example.repository;


import com.example.entity.ArticleTagEntity;
import com.example.entity.TagEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticleTagsRepository extends CrudRepository<ArticleTagEntity, Integer> {
    @Query("select tagId from ArticleTagEntity where articleId=:articleId")
    List<Integer>getArticleTagList(@Param("articleId") String articleId);
    void deleteByArticleIdAndTagId(@Param("articleId") String articleId, @Param("tagId") Integer s);
    void deleteByArticleId(String articleId);

    @Query("select t.name from ArticleTagEntity as at inner join at.tagEntity as t" +
            " where at.articleId=:articleId and at.visible=true")
    List<TagEntity> getTagListByArticleId(@Param("articleId") String articleId);
}
