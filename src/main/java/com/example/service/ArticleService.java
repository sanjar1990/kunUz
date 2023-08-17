package com.example.service;
import com.example.dto.*;
import com.example.entity.*;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.ArticleMapperInterface;
import com.example.mapper.ArticleShortInfoMapper;
import com.example.repository.*;
import com.example.utility.CheckValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.io.File;
import java.time.LocalDateTime;
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
    private TagRepository tagRepository;
    @Autowired
    private AttachRepository attachRepository;
    @Autowired
    private ArticleTypesService articleTypesService;
    @Autowired
    private ArticleTagsService articleTagsService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private CustomArticleRepository customArticleRepository;
    public ArticleDTO create(Integer moderatorId,ArticleDTO articleDTO) {
    //check
        articleDTO.setModeratorId(moderatorId);
        ArticleEntity articleEntity=new ArticleEntity();
        articleEntity.setTitle(articleDTO.getTitle());
        articleEntity.setDescription(articleDTO.getDescription());
        articleEntity.setContent(articleDTO.getContent());
        articleEntity.setCategoryId(articleDTO.getCategoryId());
        articleEntity.setRegionId(articleDTO.getRegionId());
        articleEntity.setModeratorId(moderatorId);
        articleEntity.setSharedCount(0);
        articleEntity.setViewCount(1);
        articleEntity.setLikeCount(0);
        articleEntity.setDislikeCount(0);
        if(articleDTO.getImageId()!=null){
            articleEntity.setImageId(articleDTO.getImageId());
        }
        //save article
        articleRepository.save(articleEntity);
        //save article types
        articleTypesService.create(articleEntity.getId(),articleDTO.getArticleTypes());
        // save article tags
        if(articleDTO.getTagList()!=null){
            articleTagsService.create(articleEntity.getId(),articleDTO.getTagList());
        }
        articleDTO.setId(articleEntity.getId());
        articleDTO.setStatus(articleEntity.getStatus());
        return articleDTO;
    }
    //update moderator
    public String update(ArticleDTO articleDTO, Integer moderatorId,String articleId){
        ArticleEntity articleEntity=get(articleId);
            articleEntity.setTitle(articleDTO.getTitle());
            articleEntity.setDescription(articleDTO.getDescription());
            articleEntity.setContent(articleDTO.getContent());
            articleEntity.setCategoryId(articleDTO.getCategoryId());
            articleEntity.setRegionId(articleDTO.getRegionId());
        if(articleDTO.getImageId()!=null){
            if(articleEntity.getImageId()!=null && !articleEntity.getImageId().equals(articleDTO.getImageId())){
                String url=articleEntity.getImage().getPath();
                String oldAttach=articleEntity.getImage().getId();
                File file=new File(url);
                if(file.exists()){
                    file.delete();
                }
                articleEntity.setImageId(articleDTO.getImageId());
                attachRepository.deleteById(oldAttach);
                }else {
                articleEntity.setImageId(articleDTO.getImageId());
            }
        }
        articleTypesService.merge(articleId,articleDTO.getArticleTypes());
        if(articleDTO.getTagList()!=null){
            articleTagsService.merge(articleId,articleDTO.getTagList());
        }
        articleEntity.setModeratorId(moderatorId);
        articleEntity.setStatus(ArticleStatus.NOTPUBLISHED);
        articleEntity.setPublishedDate(null);
        articleRepository.save(articleEntity);
        return "saved";
    }
    //Delete Article moderator

    public String deleteArticle(String articleId){
    return  articleRepository.deleteArticle(articleId)!=0?"Article deleted":"article not deleted";
    }
    //update status by publisher
    public String updateStatus(String  id, ArticleStatus status, Integer publisherId){
       get(id);
       int n=articleRepository.updateStatus(status, publisherId,id,LocalDateTime.now());
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
            articleDTO.setImage(attachService.getAttachURL(s.getImageId()));
            articleDTO.setPublishedDate(s.getPublishedDate());
            dtoList.add(articleDTO);
        });
        return dtoList;
    }
     // 8. Get Article By Id And Lang

    public ArticleDTO getArticleByIdAndLang(String articleId,Language language){
        ArticleEntity entity=get(articleId);
        ArticleDTO dto=new ArticleDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setSharedCount(entity.getSharedCount());
        String regionName="";
        String categoryName="";
        switch (language){
            case En -> {
                regionName=entity.getRegion().getNameEn();
                categoryName=entity.getCategory().getNameEn();
            }
            case Uz -> {
                regionName=entity.getRegion().getNameUz();
                categoryName=entity.getCategory().getNameUz();
            }
            case Ru -> {
                regionName=entity.getRegion().getNameRu();
                categoryName=entity.getCategory().getNameRu();
            }
        }
        RegionDTO regionDTO=new RegionDTO();
        regionDTO.setId(entity.getRegionId());
        regionDTO.setName(regionName);
        dto.setRegionDTO(regionDTO);
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(entity.getCategoryId());
        categoryDTO.setName(categoryName);
        dto.setCategoryDTO(categoryDTO);
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        // TODO: 7/26/2023 like count qilish
        List<TagDTO> tagDTOList=articleTagsService.getTagListByArticleId(articleId);
        dto.setTagDTOList(tagDTOList);
        return dto;
    }
//    public ArticleFullInfoMapper getByLang(String articleId, Language language) {
//        List<Object[]> objList=articleRepository.findByLang(articleId,language.name().toLowerCase());
//        ArticleFullInfoMapper mapper=new ArticleFullInfoMapper();
//        for (Object[] obj: objList) {
//            mapper.setId(obj[0].toString());
//            mapper.setTitle(obj[1].toString());
//            mapper.setDescription(obj[2].toString());
//            mapper.setContent(obj[3].toString());
//            mapper.setImageId(obj[4].toString());
//            mapper.setSharedCount(Integer.valueOf(obj[5].toString()));
//            mapper.setRegionOrderNumber(Integer.valueOf(obj[6].toString()));
//            mapper.setRegionName(obj[7].toString());
//            mapper.setCategoryOrderNumber(Integer.valueOf(obj[8].toString()));
//            mapper.setCategoryName(obj[9].toString());
//            mapper.setPublishedDate(Timestamp.valueOf(obj[10].toString()).toLocalDateTime());
//            mapper.setViewCount(Integer.valueOf(obj[11].toString()));
//            String  [] tags=obj[12].toString().split("#");
//            System.out.println(Arrays.toString(tags));
//            mapper.setTagNameList(Arrays.stream(tags).toList());
////            mapper.setTagNameList(List.of(obj[12].toString()));
//        }
//        return mapper;
//    }
    //9. Get Last 4 Article By Types and except given article id.
    //        ArticleShortInfo
    public List<ArticleDTO> getLastFour(Integer typeId, String articleId){
        List<ArticleShortInfoMapper>mapper=articleRepository.getLastFour(typeId,articleId);
        return  mapper.stream().map(s->{
            ArticleDTO articleDTO= new ArticleDTO();
            articleDTO.setId(s.getArticleId());
            articleDTO.setTitle(s.getTitle());
            articleDTO.setDescription(s.getDescription());
            articleDTO.setImage(attachService.getAttachURL(s.getImageId()));
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
        List<ArticleEntity>entityList=articleRepository.findByTypeAndRegion(typeId,regionId);
        return shortInfo(entityList);
    }
    //  13. Get Article list by Region Key (Pagination)
    public PageImpl<ArticleDTO>paginationByRegion(Integer regionId, Integer page, Integer size){
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "publishedDate"));
        Page<ArticleEntity> pageObj=articleRepository.findByRegionIdAndVisibleTrue(regionId,pageable);
        List<ArticleDTO>dtoList=shortInfo(pageObj.getContent());
        return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    //14. Get Last 5 Article Category Key
    public List<ArticleDTO> getLastFiveByCategory(Integer categoryId) {
        List<ArticleEntity> entityList=articleRepository.getLastFiveByCategory(categoryId);
        return shortInfo(entityList);
    }
    //15. Get Article By Category Key (Pagination)
    public PageImpl<ArticleDTO> getByCategoryPagination(Integer categoryId, Integer page, Integer size) {
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.DESC, "publishedDate"));
        Page<ArticleEntity> pageObj=articleRepository.findAllByCategoryIdAndVisibleTrue(categoryId,pageable);
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
            articleDTO.setImage(attachService.getAttachURL(s.getImageId()));
            articleDTO.setPublishedDate(s.getPublishedDate());
            dtoList.add(articleDTO);
        });
        return dtoList;
    }
    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO articleDTO=new ArticleDTO();
        articleDTO.setId(entity.getId());
        articleDTO.setTitle(entity.getTitle());
        articleDTO.setDescription(entity.getDescription());
        articleDTO.setContent(entity.getContent());
        articleDTO.setCategoryId(entity.getCategoryId());
        articleDTO.setRegionId(entity.getRegionId());
        List<Integer> articleTypeId=new LinkedList<>();
        articleDTO.setArticleTypes(articleTypeId);
        articleDTO.setImageId(entity.getImageId());
        articleDTO.setPublisherId(entity.getPublisherId());
        articleDTO.setStatus(entity.getStatus());
        articleDTO.setSharedCount(entity.getSharedCount());
        articleDTO.setViewCount(entity.getViewCount());
        articleDTO.setModeratorId(entity.getModeratorId());
        articleDTO.setPublishedDate(entity.getPublishedDate());
        articleDTO.setCreatedDate(entity.getCreatedDate());
        return articleDTO;
    }
    private ArticleEntity get(String articleId){
        return articleRepository.findByIdAndVisibleTrue(articleId).orElseThrow(()->new ItemNotFoundException("Article not found"));
    }
    //16. Increase Article View Count by Article Id
    public String increaseViewCount(String articleId) {
        return articleRepository.increaseViewCount(articleId)>0?"updated":"not updated";
    }
    //17. Increase Share View Count by Article Id
    public String increaseShareCount(String articleId) {
        return articleRepository.increaseShareCount(articleId)>0?"updated":"not updated";
    }

    public PageImpl<ArticleDTO> filterPagination(FilterArticleDTO filterArticleDTO, Integer page, Integer size) {
        FilterResultDTO<ArticleEntity> filterResultDTO=customArticleRepository.filterPagination(filterArticleDTO,page,size);
        Pageable pageable=PageRequest.of(page,size);
        List<ArticleDTO>dtoList=shortInfo(filterResultDTO.getContent());
        return new PageImpl<>(dtoList,pageable,filterResultDTO.getTotalElement());
    }
}
