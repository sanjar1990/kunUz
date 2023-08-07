package com.example.repository;

import com.example.entity.SavedArticleEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity,String> {
    Optional<SavedArticleEntity>findByArticleIdAndProfileIdAndVisibleTrue(String articleId, Integer profileId);
    @Transactional
    @Modifying
    int deleteByArticleIdAndProfileId(String articleId,Integer profileId);
    List<SavedArticleEntity>findByProfileIdAndVisibleTrue(Integer profileId);
}
