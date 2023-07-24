package com.example.repository;

import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.mapper.ArticleFullInfoMapper;
import com.example.mapper.ArticleMapperInterface;
import com.example.mapper.ArticleShortInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("update ArticleEntity set status=:status, publishedDate=:now," +
            " publisherId=:publisherId where id=:id and visible=true")
    int updateStatus(@Param("status") ArticleStatus status, @Param("publisherId") ProfileEntity publisherId,
                     @Param("id") String id, @Param("now")LocalDateTime now);
    //5,6

    @Query("from ArticleEntity as a" +
            " inner join a.articleTypes as at" +
            " where at.id=:id and a.status =PUBLISHED order by a.createdDate Desc limit:lim")
    List<ArticleEntity>findArticleByTape(@Param("id") Integer artId, @Param("lim") Integer limit);

    //7
    @Query(value = "select id, title, description, content, shared_count as sharedCount, image_id as imageId," +
            " region_id as regionId, category_id as categoryId, moderator_id as moderatorId, publisher_id as publisherId," +
            " status, created_date as createdDate, published_date as publishedDate, visible, view_count as viewCount" +
            "  from article " +
            "where id not in(:list) " +
            "order by published_date desc limit 8", nativeQuery = true)
    List<ArticleMapperInterface>getLastEight(@Param("list") List<String> id);
    @Query(value = "select a.id, a.title, a.description, a.content, a.image_id, a.shared_count, r.order_number," +
            " (case :lang" +
            "  when 'en' then r.name_en" +
            "  when 'uz' then r.name_uz" +
            "  when 'ru' then r.name_ru" +
            "   end) as region_name, c.order_number," +
            " (case :lang" +
            " when 'en' then c.name_en" +
            " when 'uz' then r.name_uz" +
            " when 'ru' then r.name_ru" +
            " end) as category_name, a.published_date, a.view_count, (select cast(array_agg(t.name) as Text) from article as a " +
            "inner join article_tags as at\n" +
            " on a.id=at.article_id  inner join tag as t on at.tag_id=t.id \n" +
            " where a.id='3dceb6ab-654f-4b3d-b8cc-f97665261f8c') from article as a" +
            " inner join region as r on a.region_id=r.id inner join category as c on a.category_id=c.id" +
            " where a.id=:id and a.status='PUBLISHED'", nativeQuery = true)
    List<Object[]> findByLang(@Param("id")String id, @Param("lang") String lang);
    @Transactional
    @Modifying
    @Query("update ArticleEntity set imageId=null where id=?1")
    int deletePhoto(String id);
    @Query(value = "select a.id as articleId, a.title, a.description, at.id as imageId, at.path as imageUrl," +
            "a.published_date as publishedDate " +
            "from article as a inner join attach as at " +
            " on a.image_id=at.id inner join article_releted_types as art on a.id=art.article_id" +
            " where art.article_type_id=:typeId and a.id not in(:list)" +
            " order by a.published_date desc limit 4",nativeQuery = true)
    List<ArticleShortInfoMapper> getLastFour(@Param("typeId")Integer typeId, @Param("list") List<String> idList);

      //10. Get 4 most read articles
    @Query("from ArticleEntity where visible=true order by viewCount desc limit 4")
    List<ArticleEntity>findMost();
   // 11. Get Last 4 Article By TagName (Bitta article ni eng ohirida chiqib turadi.)
    @Query("from ArticleEntity as a inner join a.tags as t where t.name=:tagName order by a.publishedDate desc limit 4")
    List<ArticleEntity> getByTag(@Param("tagName") String tagName);
    //12. Get Last 5 Article By Types  And By Region Key
    @Query("from ArticleEntity as a inner join a.articleTypes as t inner join a.regionId as r" +
            " where t.id=:typeId and r.id=:regionId order by a.publishedDate desc limit 5")
    List<ArticleEntity> findByTypeAndRegion(Integer typeId, Integer regionId);
    // 13. Get Article list by Region Key (Pagination)
    Page<ArticleEntity>findByRegionIdAndVisibleTrue(RegionEntity regionId, Pageable pageable);
}

