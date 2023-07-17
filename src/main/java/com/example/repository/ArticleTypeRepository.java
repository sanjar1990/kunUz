package com.example.repository;
import com.example.entity.ArticleTypeEntity;
import com.example.mapper.ArticleTypeLangMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity,Integer>,
        PagingAndSortingRepository<ArticleTypeEntity,Integer> {
    Boolean existsAllByNameEnOrNameUzOrNameRuOrOrderNumberAndVisibleTrue(String nameEn, String nameUz, String nameRu,Integer orderNumber);
    Optional<ArticleTypeEntity>findByOrderNumber(Integer orderNumber);
    @Transactional
    @Modifying
    @Query("update ArticleTypeEntity set visible =false where id=:id")
    int deleteArticleType(@Param("id") Integer id);
    Page<ArticleTypeEntity> findAllByVisibleTrueOrderByOrderNumberAsc(Pageable pageable);

    @Query(value = "select id, order_number as orderNumber, " +
            "(case :lang" +
            " when 'uz' then name_uz" +
            " when 'en' then name_en" +
            " when 'ru' then name_ru" +
            " end) as name " +
            " from article_type order by order_number", nativeQuery = true)
    List<ArticleTypeLangMapper> getByLang(@Param("lang") String lowerCase);
}
