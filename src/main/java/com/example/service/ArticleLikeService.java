package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.dto.ArticleLikeDTO;
import com.example.entity.ArticleEntity;
import com.example.entity.ArticleLikeEntity;
import com.example.enums.ArticleLiceStatus;
import com.example.repository.ArticleLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleLikeService {
    @Autowired
    private ArticleLikeRepository articleLikeRepository;
    //1. Like (ANY)
    public ArticleLikeDTO like(String articleId, Integer prtId) {
        Optional<ArticleLikeEntity>optional=get(articleId,prtId);
        ArticleLiceStatus status=null;
        ArticleLikeDTO dto= new ArticleLikeDTO();
        ArticleLikeEntity entity;
        if(optional.isEmpty()){
            entity=new ArticleLikeEntity();
            status=ArticleLiceStatus.LIKE;
        }else {
            entity= optional.get();
            if(entity.getStatus().equals(ArticleLiceStatus.LIKE)){
              articleLikeRepository.deleteById(entity.getId());
              return dto;
            }else if(entity.getStatus().equals(ArticleLiceStatus.DISLIKE)){
                status=ArticleLiceStatus.LIKE;
            }
        }
        entity.setArticleId(articleId);
        entity.setProfileId(prtId);
        entity.setStatus(status);
        articleLikeRepository.save(entity);
        dto.setArticleId(articleId);
        dto.setId(entity.getId());
        dto.setProfileId(prtId);
        dto.setStatus(status);
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;

    }
    public ArticleLikeDTO dislike(String articleId, Integer prtId) {
    Optional<ArticleLikeEntity>optional=get(articleId,prtId);
    ArticleLiceStatus status;
    ArticleLikeEntity entity;
    ArticleLikeDTO dto=new ArticleLikeDTO();
    if(optional.isEmpty()){
        status=ArticleLiceStatus.DISLIKE;
        entity=new ArticleLikeEntity();
    }else {
        entity= optional.get();
        if(entity.getStatus().equals(ArticleLiceStatus.LIKE)){
            status=ArticleLiceStatus.DISLIKE;
        } else {
            remove(entity.getId());
            return dto;
        }
    }
    entity.setArticleId(articleId);
    entity.setProfileId(prtId);
    entity.setStatus(status);
    articleLikeRepository.save(entity);
    dto.setId(entity.getId());
    dto.setArticleId(articleId);
    dto.setProfileId(prtId);
    dto.setStatus(status);
    dto.setCreatedDate(entity.getCreatedDate());
    return dto;
    }
    private Optional<ArticleLikeEntity> get(String articleId, Integer prtId){
        return articleLikeRepository.findByArticleIdAndProfileIdAndVisibleTrue(articleId,prtId);
    }

    public String remove(String articleLikeId){
        articleLikeRepository.deleteById(articleLikeId);
        return "article is deleted";
    }

}
