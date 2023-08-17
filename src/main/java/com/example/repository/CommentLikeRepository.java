package com.example.repository;

import com.example.entity.CommentLikeEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, String> {
    @Transactional
    @Modifying
    int deleteByIdAndProfileId(String id, Integer profileId);
   Optional<CommentLikeEntity>findAllByCommentIdAndProfileIdAndVisibleTrue(String commentId, Integer profileId);
   @Transactional
   @Modifying
   void deleteById(String id);
}
