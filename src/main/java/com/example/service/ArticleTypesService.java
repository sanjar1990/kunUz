package com.example.service;

import com.example.entity.ArticleTypesEntity;
import com.example.repository.ArticleTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTypesService {
    @Autowired
    private ArticleTypesRepository articleTypesRepository;
    public void create(String  articleId, List<Integer>typesId){
        typesId.forEach(s->create(articleId,s));
    }
    public void create(String articleId, Integer typeId){
        ArticleTypesEntity articleTypesEntity=new ArticleTypesEntity();
        articleTypesEntity.setArticleId(articleId);
        articleTypesEntity.setArticleTypeId(typeId);
        articleTypesRepository.save(articleTypesEntity);
    }
    public void merge(String articleId, List<Integer> newTypeList){
        if(newTypeList==null || newTypeList.isEmpty()){
            articleTypesRepository.deleteByArticleId(articleId);
            return;
        }
        List<Integer> oldTypeIdList=articleTypesRepository.getAllTypeId(articleId);
        newTypeList.forEach(s->{
            if(!oldTypeIdList.contains(s)){
                create(articleId,s);
            }
        });
        oldTypeIdList.forEach(s->{
            if(!newTypeList.contains(s)){
                articleTypesRepository.deleteByArticleIdAndArticleTypeId(articleId,s);
            }
        });
    }
}
