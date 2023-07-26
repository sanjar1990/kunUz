package com.example.repository;

import com.example.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, String>,
        PagingAndSortingRepository<CommentEntity, String> {
    @Transactional
    @Modifying
    @Query("update CommentEntity set visible=false where id=:id")
    int deleteComment(@Param("id") String commentId);
    @Query("from CommentEntity as c where c.articleId=:articleId and c.profileId=:profileId and c.visible=true")
    List<CommentEntity> getWithArticleId(@Param("articleId") String articleId, @Param("profileId") Integer profileId);
    Page<CommentEntity> findAllByVisibleTrue(Pageable pageable);
    List<CommentEntity>findAllByReplyIdAndVisibleTrue(String id);
}
