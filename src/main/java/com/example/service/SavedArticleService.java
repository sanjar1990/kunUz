package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.ArticleDTO;
import com.example.dto.AttachDTO;
import com.example.dto.SavedArticleDTO;
import com.example.entity.SavedArticleEntity;
import com.example.repository.SavedArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedArticleService {
    @Autowired
    private SavedArticleRepository savedArticleRepository;
    // 1. Create (ANY)
    public ApiResponseDTO save(String articleId, Integer prtId) {
    Optional<SavedArticleEntity>optional=savedArticleRepository.findByArticleIdAndProfileIdAndVisibleTrue(articleId,prtId);
    if(optional.isPresent()){
        return new ApiResponseDTO(true,"article already saved");
    }
        SavedArticleEntity entity=SavedArticleEntity.
                builder()
                .articleId(articleId)
                .profileId(prtId)
                .build();
    savedArticleRepository.save(entity);
    SavedArticleDTO dto=new SavedArticleDTO();
    dto.setArticleId(articleId);
    dto.setProfileId(prtId);
    dto.setId(entity.getId());
    dto.setCreatedDate(entity.getCreatedDate());
    return new ApiResponseDTO(true,dto);
    }

    public ApiResponseDTO delete(String articleId, Integer prtId) {
        int n=savedArticleRepository.deleteByArticleIdAndProfileId(articleId,prtId);
        return n>0?new ApiResponseDTO(true,"article is deleted"):new ApiResponseDTO(false,"article not deleted");
    }
//    3. Get Profile Saved Article List (ANY)
//        (id,article(id,title,description,image(id,url)))
    public List<SavedArticleDTO>getsavedArticleList(Integer prtId){
        List<SavedArticleEntity>entityList=savedArticleRepository.findByProfileIdAndVisibleTrue(prtId);
        return entityList.stream().map(s->{
            SavedArticleDTO dto=new SavedArticleDTO();
            dto.setId(s.getId());
            ArticleDTO articleDTO=new ArticleDTO();
            articleDTO.setId(s.getArticle().getId());
            articleDTO.setTitle(s.getArticle().getTitle());
            articleDTO.setDescription(s.getArticle().getDescription());
            AttachDTO attachDTO=new AttachDTO();
            attachDTO.setId(s.getArticle().getImage().getId());
            attachDTO.setUrl(s.getArticle().getImage().getPath());
            articleDTO.setImage(attachDTO);
            dto.setArticle(articleDTO);
            return dto;
        }).toList();

    }
}
