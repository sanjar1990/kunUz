package com.example.repository;

import com.example.dto.FilterArticleDTO;
import com.example.dto.FilterResultDTO;
import com.example.entity.ArticleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomArticleRepository {
    @Autowired
    private EntityManager entityManager;
    public FilterResultDTO<ArticleEntity>filterPagination(FilterArticleDTO filterArticleDTO, Integer page, Integer size){
        StringBuilder selectBuilder=new StringBuilder("from ArticleEntity as a");
        StringBuilder countBuilder=new StringBuilder("select count(a) from ArticleEntity as a");
        StringBuilder builder=new StringBuilder(" where a.visible=true");
        Map<String,Object>params=new HashMap<>();
        if(filterArticleDTO.getTitle()!=null){
            builder.append(" and a.title like :title");
            params.put("title","%"+filterArticleDTO.getTitle()+"%");
        }
        if(filterArticleDTO.getRegionId()!=null){
            builder.append(" and a.regionId = :regionId");
            params.put("regionId",filterArticleDTO.getRegionId());
        }
        if(filterArticleDTO.getCategoryId()!=null){
            builder.append(" and a.categoryId=:categoryId");
            params.put("categoryId",filterArticleDTO.getCategoryId());
        }
        if(filterArticleDTO.getCratedDateFrom()!=null){
             builder.append(" and a.createdDate>=:cFrom");
             params.put("from",filterArticleDTO.getCratedDateFrom().atStartOfDay());
        }
        if(filterArticleDTO.getCreatedDateTo()!=null){
            builder.append(" and a.createdDate<=:cTo");
            params.put("to", LocalDateTime.of(filterArticleDTO.getCreatedDateTo(), LocalTime.MAX));
        }
        if(filterArticleDTO.getPublishedDateFrom()!=null){
            builder.append(" and a.publishedDate>=:pFrom");
            params.put("pFrom",filterArticleDTO.getPublishedDateFrom().atStartOfDay());
        }
        if(filterArticleDTO.getPublishedDateTo()!=null){
            builder.append(" and a.publishedDate<=:pTo");
            params.put("pTo",LocalDateTime.of(filterArticleDTO.getPublishedDateTo(),LocalTime.MAX));
        }
        if(filterArticleDTO.getModeratorId()!=null){
            builder.append(" and a.moderatorId=:moderatorId");
            params.put("moderatorId",filterArticleDTO.getModeratorId());
        }
        if(filterArticleDTO.getPublisherId()!=null){
            builder.append(" and a.publisherId=:publisherId");
            params.put("publisherId",filterArticleDTO.getPublisherId());
        }
        if(filterArticleDTO.getStatus()!=null){
            builder.append(" and a.status=:status");
            params.put("status",filterArticleDTO.getStatus());
        }
        countBuilder.append(builder);
        builder.append(" order by a.createdDate");
        selectBuilder.append(builder);
        Query selectQuery=entityManager.createQuery(selectBuilder.toString());
        Query countQuery=entityManager.createQuery(countBuilder.toString());
        for (Map.Entry<String,Object> p: params.entrySet()){
            selectQuery.setParameter(p.getKey(),p.getValue());
            countQuery.setParameter(p.getKey(),p.getValue());
        }
        selectQuery.setFirstResult(page*size);
        selectQuery.setMaxResults(size);
        List<ArticleEntity> entityList=selectQuery.getResultList();
        Long totalElement=(Long)countQuery.getSingleResult();
        return new FilterResultDTO<>(entityList,totalElement);
    }
}
