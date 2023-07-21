package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.entity.ArticleTypeEntity;
import com.example.enums.ArticleStatus;
import com.example.mapper.ArticleMapperInterface;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ArticleRepository extends CrudRepository<ArticleEntity, String> {

    Optional<ArticleEntity>findByIdAndVisibleTrue(String  id);
    @Transactional
    @Modifying
    @Query("update ArticleEntity set visible=false where id=?1 ")
    int deleteArticle(String articleId);
    @Transactional
    @Modifying
    @Query("update ArticleEntity set status=:status, publishedDate=:now where id=:id and visible=true")
    int updateStatus(@Param("status") ArticleStatus status, @Param("id") String id, @Param("now")LocalDateTime now);
    //5,6

    @Query("from ArticleEntity as a" +
            " inner join a.articleTypes as at" +
            " where at.id=:id and a.status =:status order by a.createdDate Desc limit:lim")
    List<ArticleEntity>findArticleByTape(@Param("id") Integer artId, @Param("status") ArticleStatus status,@Param("lim") Integer limit);

    //7
    @Query(value = "select id, title, description, content, shared_count as sharedCount, image_id as imageId," +
            " region_id as regionId, category_id as categoryId, moderator_id as moderatorId, publisher_id as publisherId," +
            " status, created_date as createdDate, published_date as publishedDate, visible, view_count as viewCount" +
            "  from article " +
            "where id not in(:list) " +
            "order by created_date desc limit 8", nativeQuery = true)
    List<ArticleMapperInterface>getLastEight(@Param("list") List<String> id);
}

