package com.example.repository;

import com.example.dto.FilterCommentDTO;
import com.example.dto.FilterResultDTO;
import com.example.entity.CommentEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomCommentRepository {
    @Autowired
    private EntityManager entityManager;

//    6. Comment Filter(created_date_from,created_date_to,profile_id,article_id) with Pagination (ADMIN)
//    id,created_date,update_date,profile_id,content,article_id,reply_id,visible
    public FilterResultDTO<CommentEntity> filterPagination(FilterCommentDTO filterCommentDTO, Integer page, Integer size) {
        StringBuilder selectBuilder=new StringBuilder("from CommentEntity as c");
        StringBuilder countBuilder=new StringBuilder("select count(c) from CommentEntity as c");
        StringBuilder builder=new StringBuilder(" where visible=true");
        Map<String, Object> params=new HashMap<>();
        if(filterCommentDTO.getCreatedDateFrom()!=null){
            builder.append(" and c.createdDate>=:from");
            params.put("from", LocalDateTime.of(filterCommentDTO.getCreatedDateFrom(), LocalTime.MIN));
        }
        if(filterCommentDTO.getCreatedDateTo()!=null){
            builder.append(" and c.createdDate<=:to");
            params.put("to",LocalDateTime.of(filterCommentDTO.getCreatedDateTo(), LocalTime.MAX));
        }
        if(filterCommentDTO.getProfileId()!=null){
            builder.append(" and c.profileId=:profileId");
            params.put("profileId",filterCommentDTO.getProfileId());
        }
        if(filterCommentDTO.getArticleId()!=null){
            builder.append(" and c.articleId=:articleId");
            params.put("articleId",filterCommentDTO.getArticleId());
        }
        countBuilder.append(builder);
        builder.append(" order by c.createdDate");
        selectBuilder.append(builder);
        Query selectQuery=entityManager.createQuery(selectBuilder.toString());
        Query countQuery=entityManager.createQuery(countBuilder.toString());
        for (Map.Entry<String,Object> p: params.entrySet()) {
            selectQuery.setParameter(p.getKey(),p.getValue());
            countQuery.setParameter(p.getKey(),p.getValue());
        }
        selectQuery.setFirstResult(page*size);
        selectQuery.setMaxResults(size);
        List<CommentEntity> entityList=selectQuery.getResultList();
        Long totalElement=(Long) countQuery.getSingleResult();
        return new FilterResultDTO<>(entityList,totalElement);
    }
}
