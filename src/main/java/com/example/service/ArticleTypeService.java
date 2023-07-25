package com.example.service;

import com.example.dto.ArticleTypeDTO;
import com.example.entity.ArticleTypeEntity;
import com.example.entity.RegionEntity;
import com.example.enums.Language;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemAlreadyExists;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.ArticleTypeLangMapper;
import com.example.repository.ArticleTypeRepository;
import com.example.utility.CheckValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;
    @Autowired
    private CheckValidationUtility checkValidationUtility;
    //1 create by admin
    public ArticleTypeDTO create(ArticleTypeDTO articleTypeDTO,Integer prtId){
        checkValidationUtility.checkArticleType(articleTypeDTO);
        Boolean isExists=articleTypeRepository
                .existsAllByNameEnOrNameUzOrNameRuOrOrderNumberAndVisibleTrue(articleTypeDTO.getNameEn(),
                        articleTypeDTO.getNameUz(),articleTypeDTO.getNameRu(),articleTypeDTO.getOrderNumber());
        if (isExists) throw new ItemAlreadyExists("This Region already exists");
        ArticleTypeEntity articleTypeEntity=toEntity(articleTypeDTO);
        articleTypeEntity.setPrtId(prtId);
        articleTypeRepository.save(articleTypeEntity);
        articleTypeDTO.setVisible(articleTypeEntity.getVisible());
        articleTypeDTO.setCreatedDate(articleTypeEntity.getCreatedDate());
        articleTypeDTO.setId(articleTypeEntity.getId());
        articleTypeDTO.setPrtId(prtId);
        return articleTypeDTO;
    }
    //2 update admin
    public String update(ArticleTypeDTO articleTypeDTO, Integer id,Integer prtId){
       ArticleTypeEntity articleTypeEntity=get(id);
        Boolean isExists=articleTypeRepository
                .existsAllByNameEnOrNameUzOrNameRuOrOrderNumberAndVisibleTrue
                        (articleTypeDTO.getNameEn(),articleTypeDTO.getNameRu(),articleTypeDTO.getNameUz(),articleTypeDTO.getOrderNumber());
        if (isExists) throw new ItemAlreadyExists(" Article Type already exists");
        if(articleTypeDTO.getNameEn()!=null){
            articleTypeEntity.setNameEn(articleTypeDTO.getNameEn());
        }
        if (articleTypeDTO.getNameUz()!=null){
            articleTypeEntity.setNameUz(articleTypeDTO.getNameUz());
        }
        if(articleTypeDTO.getNameRu()!=null){
            articleTypeEntity.setNameRu(articleTypeDTO.getNameRu());
        }
        if(articleTypeDTO.getVisible()!=null){
            articleTypeEntity.setVisible(articleTypeDTO.getVisible());
        }
        if (articleTypeDTO.getOrderNumber()!=null){
            articleTypeEntity.setOrderNumber(articleTypeDTO.getOrderNumber());
        }
        articleTypeEntity.setPrtId(prtId);
        articleTypeRepository.save(articleTypeEntity);
        return "Article type updated";
    }

    //3 delete admin
    public String delete(Integer id){
        return articleTypeRepository.deleteArticleType(id)>0?"type deleted":"type not deleted";
    }
    //4 get all by admin
    public PageImpl<ArticleTypeDTO> getAll(Integer page, Integer size){
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.ASC,"orderNumber"));
       Page<ArticleTypeEntity> pageObj=articleTypeRepository.findAllByVisibleTrueOrderByOrderNumberAsc(pageable);
       List<ArticleTypeDTO> dtoList=pageObj.getContent().stream().map(s->toDto(s)).toList();
       return new PageImpl<>(dtoList,pageable,pageObj.getTotalElements());
    }
    public List<ArticleTypeDTO>getByLanguage(Language language){
        List<ArticleTypeLangMapper> mapperList=articleTypeRepository.getByLang(language.name().toLowerCase());
        List<ArticleTypeDTO> dtoList=new LinkedList<>();
        mapperList.forEach(s->{
            ArticleTypeDTO articleTypeDTO = new ArticleTypeDTO();
            articleTypeDTO.setId(s.getId());
            articleTypeDTO.setOrderNumber(s.getOrderNumber());
            articleTypeDTO.setName(s.getName());
            dtoList.add(articleTypeDTO);
        });
        System.out.println(dtoList);
        return dtoList;
    }
    public ArticleTypeEntity toEntity(ArticleTypeDTO articleTypeDTO){
        ArticleTypeEntity articleTypeEntity=new ArticleTypeEntity();
        articleTypeEntity.setOrderNumber(articleTypeDTO.getOrderNumber());
        articleTypeEntity.setNameEn(articleTypeDTO.getNameEn());
        articleTypeEntity.setNameUz(articleTypeDTO.getNameUz());
        articleTypeEntity.setNameRu(articleTypeDTO.getNameRu());
        return articleTypeEntity;
    }
    public ArticleTypeDTO toDto(ArticleTypeEntity articleTypeEntity){
        ArticleTypeDTO articleTypeDTO=new ArticleTypeDTO();
        articleTypeDTO.setVisible(articleTypeEntity.getVisible());
        articleTypeDTO.setId(articleTypeEntity.getId());
        articleTypeDTO.setCreatedDate(articleTypeEntity.getCreatedDate());
        articleTypeDTO.setOrderNumber(articleTypeEntity.getOrderNumber());
        articleTypeDTO.setNameEn(articleTypeEntity.getNameEn());
        articleTypeDTO.setNameUz(articleTypeEntity.getNameUz());
        articleTypeDTO.setNameRu(articleTypeEntity.getNameRu());
        articleTypeDTO.setPrtId(articleTypeEntity.getPrtId());
        return articleTypeDTO;
    }
    private ArticleTypeEntity get(Integer id){
        return articleTypeRepository.findById(id).orElseThrow(()->new ItemNotFoundException("Article type not found"));
    }

}
