package com.example.service;

import com.example.dto.ArticleDTO;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.ArticleFullInfoMapper;
import com.example.mapper.ArticleMapperInterface;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.repository.*;
import com.example.utility.CheckValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AttachRepository attachRepository;
    public ArticleDTO create(Integer id,ArticleDTO articleDTO) {
    //check
        checkValidationUtility.checkForArticle(articleDTO);
        articleDTO.setModeratorId(id);
        Optional<CategoryEntity>category=categoryRepository.findByIdAndVisibleTrue(articleDTO.getCategoryId());
        Optional<RegionEntity> region=regionRepository.findByIdAndVisibleTrue(articleDTO.getRegionId());
        Optional<ProfileEntity>moderator=profileRepository.findByIdAndVisibleTrue(id);
        List<ArticleTypeEntity> allTypes=articleTypeRepository.findAllByVisibleTrue();
        List<ArticleTypeEntity>artTypes=new LinkedList<>();
        if(!articleDTO.getArticleTypes().isEmpty()){
            for (ArticleTypeEntity n: allTypes){
                for (Integer i:articleDTO.getArticleTypes()){
                    if (n.getId().equals(i)){
                        artTypes.add(n);
                    }
                }
            }
        }
        List<TagEntity> tagEntityList=tagRepository.findAllByVisibleTrue();
        List<TagEntity> selectedTagList=new LinkedList<>();
        if(!articleDTO.getTags().isEmpty()){
            for (TagEntity t: tagEntityList) {
                for (Integer i:articleDTO.getTags()) {
                    if(t.getId().equals(i)){
                        selectedTagList.add(t);
                    }
                }
            }
        }
        if(category.isEmpty()) throw new ItemNotFoundException("Category not found");
        if(region.isEmpty()) throw new ItemNotFoundException("Region not found");
        if(moderator.isEmpty())throw new ItemNotFoundException("moderator not found");
        ArticleEntity articleEntity=new ArticleEntity();
        articleEntity.setTitle(articleDTO.getTitle());
        articleEntity.setDescription(articleDTO.getDescription());
        articleEntity.setContent(articleDTO.getContent());
        articleEntity.setCategoryId(category.get());
        articleEntity.setArticleTypes(artTypes);
        articleEntity.setRegionId(region.get());
        articleEntity.setModeratorId(moderator.get());
        articleEntity.setSharedCount(articleDTO.getSharedCount());
        articleEntity.setTags(selectedTagList);
        articleEntity.setViewCount(articleDTO.getViewCount());
        if(articleDTO.getImageId()!=null){
            Optional<AttachEntity>optional=attachRepository.findById(articleDTO.getImageId());
            if(optional.isEmpty()) throw new ItemNotFoundException("Attach not found");
            articleEntity.setImageId(optional.get());
        }
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
            articleEntity.setPublishedDate(articleDTO.getPublishedDate());
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
            Optional<AttachEntity>attach=attachRepository.findById(articleDTO.getImageId());
            if(attach.isEmpty()) throw new ItemNotFoundException("Attach not found");
            if(articleEntity.getImageId()!=null){
                String url=articleEntity.getImageId().getPath();
                File file=new File(url);
                if(file.exists()){
                    file.delete();
                }
               int n= articleRepository.deletePhoto(articleEntity.getId());
                if(n>0){
                    attachRepository.deleteById(articleEntity.getImageId().getId());
                }
                }

            articleEntity.setImageId(attach.get());
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

    //update status by publisher
    public String updateStatus(String  id, ArticleStatus status, Integer publisherId){
        Optional<ArticleEntity>optional=articleRepository.findByIdAndVisibleTrue(id);
        Optional<ProfileEntity>publisher=profileRepository.findById(publisherId);
        if(optional.isEmpty()) throw new ItemNotFoundException("Article not found");
        if(publisher.isEmpty()) throw new ItemNotFoundException("Publisher not found");
       int n=articleRepository.updateStatus(status, publisher.get(),id, LocalDateTime.now());
        System.out.println(status);
        return n>0?"Article status updated":"status not updated";
    }
    //5 5. Get Last 5 Article By Types  ordered_by_created_date
    //6. Get Last 3 Article By Types  ordered_by_created_date
    public List<ArticleDTO> getLastArticle(Integer typeId, Integer limit){
        List<ArticleEntity> entityList=articleRepository.findArticleByTape(typeId,limit);
       return shortInfo(entityList);
    }
    //7 get last 8
    public List<ArticleDTO>getLastEight(List<String> idList){
        if (idList==null) throw new AppBadRequestException("Id not found");
        List<ArticleMapperInterface> mapperList=articleRepository.getLastEight(idList);
        List<ArticleDTO> dtoList=new LinkedList<>();
        mapperList.forEach(s->{
            ArticleDTO articleDTO=new ArticleDTO();
            articleDTO.setId(s.getId());
            articleDTO.setTitle(s.getTitle());
            articleDTO.setDescription(s.getDescription());
            articleDTO.setImageId(s.getImageId());
            articleDTO.setPublishedDate(s.getPublishedDate());
            dtoList.add(articleDTO);
        });
        return dtoList;
    }
     // 8. Get Article By Id And Lang
    public ArticleFullInfoMapper getByLang(String articleId, Language language) {
        List<Object[]> objList=articleRepository.findByLang(articleId,language.name().toLowerCase());
        ArticleFullInfoMapper mapper=new ArticleFullInfoMapper();
        for (Object[] obj: objList) {
            mapper.setId(obj[0].toString());
            mapper.setTitle(obj[1].toString());
            mapper.setDescription(obj[2].toString());
            mapper.setContent(obj[3].toString());
            mapper.setImageId(obj[4].toString());
            mapper.setSharedCount(Integer.valueOf(obj[5].toString()));
            mapper.setRegionOrderNumber(Integer.valueOf(obj[6].toString()));
            mapper.setRegionName(obj[7].toString());
            mapper.setCategoryOrderNumber(Integer.valueOf(obj[8].toString()));
            mapper.setCategoryName(obj[9].toString());
            mapper.setPublishedDate(Timestamp.valueOf(obj[10].toString()).toLocalDateTime());
            mapper.setViewCount(Integer.valueOf(obj[11].toString()));
            String  [] tags=obj[12].toString().split("#");
            System.out.println(Arrays.toString(tags));
            mapper.setTagNameList(Arrays.stream(tags).toList());
//            mapper.setTagNameList(List.of(obj[12].toString()));
        }
        return mapper;
    }
    //9. Get Last 4 Article By Types and except given article id.
    //        ArticleShortInfo
    public List<ArticleDTO> getLastFour(Integer typeId, List<String>idList){
        Optional<ArticleTypeEntity>articleType=articleTypeRepository.findByIdAndVisibleTrue(typeId);
        if(articleType.isEmpty())throw new ItemNotFoundException("Article type not found!");

        List<ArticleShortInfoMapper>mapper=articleRepository.getLastFour(typeId,idList);
        return  mapper.stream().map(s->{
            ArticleDTO articleDTO= new ArticleDTO();
            articleDTO.setId(s.getArticleId());
            articleDTO.setTitle(s.getTitle());
            articleDTO.setDescription(s.getDescription());
            articleDTO.setImageId(s.getImageId());
            articleDTO.setImageUrl(s.getImageUrl());
            articleDTO.setPublishedDate(s.getPublishedDate());
            return articleDTO;
        }).toList();
    }
    //  10. Get 4 most read articles
    public List<ArticleDTO>fourMostRead(){
        List<ArticleEntity> entityList=articleRepository.findMost();
        return shortInfo(entityList);
    }
    // 11. Get Last 4 Article By TagName (Bitta article ni eng ohirida chiqib turadi.)
    public List<ArticleDTO> getByTag(String tagName){
        Optional<TagEntity> tag=tagRepository.findByNameAndVisibleTrue(tagName);
        if (tag.isEmpty())throw new ItemNotFoundException("tag not found");
        List<ArticleEntity> entityList=articleRepository.getByTag(tag.get().getName());
        return shortInfo(entityList);
    }
    //12. Get Last 5 Article By Types  And By Region Key
    public List<ArticleDTO>getByTypeAndRegion(Integer typeId, Integer regionId){
        Optional<ArticleTypeEntity>type=articleTypeRepository.findByIdAndVisibleTrue(typeId);
        Optional<RegionEntity>region=regionRepository.findByIdAndVisibleTrue(regionId);
        if(type.isEmpty()) throw new ItemNotFoundException("type not found");
        if(region.isEmpty()) throw new ItemNotFoundException("region not found");
        List<ArticleEntity>entityList=articleRepository.findByTypeAndRegion(typeId,regionId);
        return shortInfo(entityList);
    }
    //  13. Get Article list by Region Key (Pagination)
    public PageImpl<ArticleDTO>paginationByRegion(Integer regionId, Integer page, Integer size){
        Optional<RegionEntity>optional=regionRepository.findByIdAndVisibleTrue(regionId);
        if (optional.isEmpty()) throw new ItemNotFoundException("");
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "publishedDate"));
        Page<ArticleEntity> pageObj=articleRepository.findByRegionIdAndVisibleTrue(optional.get(),pageable);
        List<ArticleDTO>dtoList=shortInfo(pageObj.getContent());
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    private List<ArticleDTO> shortInfo(List<ArticleEntity> entityList){
        List<ArticleDTO> dtoList=new LinkedList<>();
        entityList.forEach(s->{
            ArticleDTO articleDTO=new ArticleDTO();
            articleDTO.setId(s.getId());
            articleDTO.setTitle(s.getTitle());
            articleDTO.setDescription(s.getDescription());
            articleDTO.setImageId(s.getImageId().getId());
            articleDTO.setPublishedDate(s.getPublishedDate());
            dtoList.add(articleDTO);
        });
        return dtoList;
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
        articleDTO.setImageId(entity.getImageId().getId());
        articleDTO.setPublisherId(entity.getPublisherId().getId());
        articleDTO.setStatus(entity.getStatus());
        articleDTO.setSharedCount(entity.getSharedCount());
        articleDTO.setViewCount(entity.getViewCount());
        articleDTO.setModeratorId(entity.getModeratorId().getId());
        articleDTO.setPublishedDate(entity.getPublishedDate());
        articleDTO.setCreatedDate(entity.getCreatedDate());
        return articleDTO;
    }



}
