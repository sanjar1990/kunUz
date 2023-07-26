package com.example.service;

import com.example.dto.TagDTO;
import com.example.entity.ArticleTagEntity;
import com.example.entity.TagEntity;
import com.example.repository.ArticleTagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleTagsService {
     @Autowired
    private ArticleTagsRepository articleTagsRepository;
    public void create(String articleId, List<Integer>tagList){
        tagList.forEach(s->create(articleId,s));
    }
    public void create(String articleId, Integer tagId){
        ArticleTagEntity articleTagEntity= new ArticleTagEntity();
        articleTagEntity.setArticleId(articleId);
        articleTagEntity.setTagId(tagId);
        articleTagsRepository.save(articleTagEntity);
    }
    public void merge(String articleId,List<Integer>newTagList){
        if(newTagList==null || newTagList.isEmpty()){
            articleTagsRepository.deleteByArticleId(articleId);
        }
        List<Integer> oldTagList=articleTagsRepository.getArticleTagList(articleId);
        newTagList.forEach(s->{
            if(!oldTagList.contains(s)){
                create(articleId,s);
            }
        });
        oldTagList.forEach(s->{
            if (!newTagList.contains(s)){
                articleTagsRepository.deleteByArticleIdAndTagId(articleId,s);
            }
        });
    }

    public List<TagDTO> getTagListByArticleId(String articleId){
       List<TagEntity> tagEntityList=articleTagsRepository.getTagListByArticleId(articleId);
        return tagEntityList.stream().map(s->{
           TagDTO tagDTO=new TagDTO();
           tagDTO.setName(s.getName());
           tagDTO.setId(s.getId());
           return tagDTO;
       }).toList();
    }
}
