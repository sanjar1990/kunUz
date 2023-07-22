package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.ArticleMapperInterface;
import com.example.mapper.ArticleTypeListMapper;
import com.example.repository.*;
import com.example.utility.CheckValidationUtility;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CheckValidationUtility checkValidationUtility;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ArticleTypeRepository articleTypeRepository;
    public ArticleDTO create(Integer id,ArticleDTO articleDTO) {
    //check
        checkValidationUtility.checkForArticle(articleDTO);
        articleDTO.setModeratorId(id);
        Optional<CategoryEntity>category=categoryRepository.findByIdAndVisibleTrue(articleDTO.getCategoryId());
        Optional<RegionEntity> region=regionRepository.findByIdAndVisibleTrue(articleDTO.getRegionId());
        Optional<ProfileEntity>publisher=profileRepository.findByIdAndVisibleTrue(articleDTO.getPublisherId());
        Optional<ProfileEntity>moderator=profileRepository.findByIdAndVisibleTrue(id);

        List<ArticleTypeEntity> allTypes=articleTypeRepository.findAllByVisibleTrue();
        List<ArticleTypeEntity>artTypes=new LinkedList<>();
        for (ArticleTypeEntity n: allTypes){
            for (Integer i:articleDTO.getArticleTypes()){
                if (n.getId().equals(i)){
                    artTypes.add(n);
                }
            }
        }
        if(category.isEmpty()) throw new ItemNotFoundException("Category not found");
        if(region.isEmpty()) throw new ItemNotFoundException("Region not found");
        if(publisher.isEmpty())throw new ItemNotFoundException("publisher not found");
        if(moderator.isEmpty())throw new ItemNotFoundException("moderator not found");
        ArticleEntity articleEntity=new ArticleEntity();
        articleEntity.setTitle(articleDTO.getTitle());
        articleEntity.setDescription(articleDTO.getDescription());
        articleEntity.setContent(articleDTO.getContent());
        articleEntity.setCategoryId(category.get());
        articleEntity.setPublisherId(publisher.get());
        articleEntity.setArticleTypes(artTypes);
//        articleEntity.setPublishedDate(LocalDateTime.of(articleDTO.getPublishedDate(),LocalTime.MIN));
        articleEntity.setRegionId(region.get());
        articleEntity.setModeratorId(moderator.get());
        articleEntity.setSharedCount(articleDTO.getSharedCount());
        articleRepository.save(articleEntity);
        articleDTO.setId(articleEntity.getId());
        articleDTO.setStatus(articleEntity.getStatus());
        return articleDTO;
    }

    //update moderator
    public String update(ArticleDTO articleDTO, Integer moderatorId,String articleId){
        Optional<ArticleEntity>optional=articleRepository.findByIdAndVisibleTrue(articleId);
        if(optional.isEmpty()) throw new ItemNotFoundException("Article not found");
        ArticleEntity articleEntity=optional.get();
        if(!articleEntity.getModeratorId().getId().equals(moderatorId)){
            throw new AppBadRequestException("This is not your id");
        }
        if (articleDTO.getTitle()!=null){
            articleEntity.setTitle(articleDTO.getTitle());
        }
        if(articleDTO.getDescription()!=null){
            articleEntity.setDescription(articleDTO.getDescription());
        }
        if(articleDTO.getContent()!=null){
            articleEntity.setContent(articleDTO.getContent());
        }
        if(articleDTO.getCategoryId()!=null){
            Optional<CategoryEntity>category=categoryRepository.findByIdAndVisibleTrue(articleDTO.getCategoryId());
            if(category.isEmpty())throw new ItemNotFoundException("Category not found");
            articleEntity.setCategoryId(category.get());
        }
        if(articleDTO.getRegionId()!=null){
            Optional<RegionEntity>region=regionRepository.findByIdAndVisibleTrue(articleDTO.getRegionId());
            if(region.isEmpty())throw new ItemNotFoundException("region not found");
            articleEntity.setRegionId(region.get());
        }
        if(articleDTO.getPublishedDate()!=null){
            articleEntity.setPublishedDate(articleDTO.getPublishedDate().atStartOfDay());
        }
        if(articleDTO.getPublisherId()!=null){
            Optional<ProfileEntity> publisher=profileRepository.findByIdAndVisibleTrue(articleDTO.getPublisherId());
            if(publisher.isEmpty()) throw new ItemNotFoundException("Publisher not found");
            articleEntity.setPublisherId(publisher.get());
        }
        if(articleDTO.getStatus()!=null){
            articleEntity.setStatus(articleDTO.getStatus());
        }
        if(articleDTO.getImageId()!=null){
            articleEntity.setImageId(articleDTO.getImageId());
        }
        if(articleDTO.getSharedCount()!=null){
            articleEntity.setSharedCount(articleDTO.getSharedCount());
        }
        if(articleDTO.getArticleTypes()!=null ){
            List<ArticleTypeEntity> allTypes=articleTypeRepository.findAllByVisibleTrue();
            List<ArticleTypeEntity>artTypes=new LinkedList<>();
            for (ArticleTypeEntity n: allTypes){
                for (Integer i:articleDTO.getArticleTypes()){
                    if (n.getId().equals(i)){
                        artTypes.add(n);
                    }
                }
            }
            articleEntity.setArticleTypes(artTypes);
        }
        Optional<ProfileEntity>moderator=profileRepository.findByIdAndVisibleTrue(moderatorId);
        if(moderator.isEmpty()) throw new ItemNotFoundException("Publisher not found");
        articleEntity.setModeratorId(moderator.get());
        articleRepository.save(articleEntity);
        return "saved";
    }
    //Delete Article moderator

    public String deleteArticle(String articleId){
    return  articleRepository.deleteArticle(articleId)!=0?"Article deleted":"article not deleted";
    }

    //update status by moderator
    public String updateStatus(String  id, ArticleDTO articleDTO){
        Optional<ArticleEntity>optional=articleRepository.findByIdAndVisibleTrue(id);
        if(optional.isEmpty()) throw new ItemNotFoundException("Article not found");
       int n=articleRepository.updateStatus(articleDTO.getStatus(),id,LocalDateTime.now());
        System.out.println(articleDTO.getStatus());
        return n>0?"Article status updated":"status not updated";
    }
    //5 5. Get Last 5 Article By Types  ordered_by_created_date
    public List<ArticleDTO> getLastFiveArticle(ArticleTypeListMapper<Integer> articleTypeListMapper){
       List<ArticleEntity> entityList=new LinkedList<>();
       for (Integer n: articleTypeListMapper.getArticleTypes()){
           List<ArticleEntity> list=articleRepository.findArticleByTape(n,ArticleStatus.PUBLISHED,5);
           entityList.addAll(list);
       }

       return entityList.stream().map(s->toDTO(s)).toList();
    }
    //6. Get Last 3 Article By Types  ordered_by_created_date
    public List<ArticleDTO> getLastThreeByType(ArticleTypeListMapper<Integer> articleTypeListMapper){
        List<ArticleEntity>entityList=new LinkedList<>();
        for (Integer n: articleTypeListMapper.getArticleTypes()){
            List<ArticleEntity>list=articleRepository.findArticleByTape(n,ArticleStatus.PUBLISHED,3);
            entityList.addAll(list);
        }
        return entityList.stream().map(s->toDTO(s)).toList();
    }
    //7 get last 8
    public List<ArticleDTO>getLastEight(List<String> idList){
        if (idList==null) throw new AppBadRequestException("Id not found");
        List<ArticleMapperInterface> mapperInterfaceList=articleRepository.getLastEight(idList);
        return mapperInterfaceList.stream().map(s->toDTOFromMapper(s)).toList();
    }

    //8. Get Article By Id And Lang
//    public List<ArticleDTO>getByLang(Language language)
    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO articleDTO=new ArticleDTO();
        articleDTO.setId(entity.getId());
        articleDTO.setTitle(entity.getTitle());
        articleDTO.setDescription(entity.getDescription());
        articleDTO.setContent(entity.getContent());
        articleDTO.setCategoryId(entity.getCategoryId().getId());
        articleDTO.setRegionId(entity.getRegionId().getId());
        List<Integer> articleTypeId=new LinkedList<>();
        for (ArticleTypeEntity at: entity.getArticleTypes()){
            articleTypeId.add(at.getId());
        }
        articleDTO.setArticleTypes(articleTypeId);
        articleDTO.setImageId(entity.getImageId());
        articleDTO.setPublisherId(entity.getPublisherId().getId());
        articleDTO.setStatus(entity.getStatus());
        articleDTO.setSharedCount(entity.getSharedCount());
        articleDTO.setViewCount(entity.getViewCount());
        articleDTO.setModeratorId(entity.getModeratorId().getId());
        articleDTO.setPublishedDate(entity.getPublishedDate().toLocalDate());
        articleDTO.setCreatedDate(entity.getCreatedDate());
        return articleDTO;
    }
    private ArticleDTO toDTOFromMapper(ArticleMapperInterface entity) {
        ArticleDTO articleDTO=new ArticleDTO();
        articleDTO.setId(entity.getId());
        articleDTO.setTitle(entity.getTitle());
        articleDTO.setDescription(entity.getDescription());
        articleDTO.setContent(entity.getContent());
        articleDTO.setCategoryId(entity.getCategoryId());
        articleDTO.setRegionId(entity.getRegionId());
        articleDTO.setImageId(entity.getImageId());
        articleDTO.setPublisherId(entity.getPublisherId());
        articleDTO.setStatus(ArticleStatus.valueOf(entity.getStatus()));
        articleDTO.setSharedCount(entity.getSharedCount());
        articleDTO.setViewCount(entity.getViewCount());
        articleDTO.setModeratorId(entity.getModeratorId());
        articleDTO.setPublishedDate(entity.getPublishedDate().toLocalDate());
        articleDTO.setCreatedDate(entity.getCreatedDate());
        return articleDTO;
    }


}
