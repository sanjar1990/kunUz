package com.example.repository;

import com.example.entity.ArticleLikeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity,String> {
    Optional<ArticleLikeEntity>findByArticleIdAndProfileIdAndVisibleTrue(String articleId,Integer profileId);
}
