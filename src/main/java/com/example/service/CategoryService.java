package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.enums.Language;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemAlreadyExists;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.CategoryLanguageMapper;
import com.example.repository.CategoryRepository;
import com.example.utility.CheckValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    // 1 create by admin
    public CategoryDTO createCategory(CategoryDTO categoryDTO,Integer prtId){
        Boolean exists=categoryRepository
                .existsAllByNameEnOrNameUzOrNameRuOrOrderNumber(categoryDTO.getNameEn(),
                        categoryDTO.getNameUz(), categoryDTO.getNameRu(),categoryDTO.getOrderNumber());
        if(exists)throw new ItemAlreadyExists("This category is exists");
        CategoryEntity categoryEntity=toEntity(categoryDTO);
        categoryEntity.setPrtId(prtId);
        categoryRepository.save(categoryEntity);
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        categoryDTO.setVisible(categoryEntity.getVisible());
        categoryDTO.setPrtId(prtId);
        return categoryDTO;
    }
    //2 update by admin
    @Cacheable(value = "category",key = "#id")
    public String updateCategory(CategoryDTO categoryDTO, Integer id, Integer prtId){
       CategoryEntity categoryEntity=getCategoryEntity(id);
            Boolean isExists=categoryRepository
                .existsAllByNameEnOrNameUzOrNameRuOrOrderNumber(categoryDTO.getNameEn(),
                        categoryDTO.getNameUz(), categoryDTO.getNameRu(),categoryDTO.getOrderNumber());
        if(isExists) {
            throw new ItemAlreadyExists("this category already exists");
        }
         categoryEntity.setOrderNumber(categoryDTO.getOrderNumber());
         categoryEntity.setNameEn(categoryDTO.getNameEn());
         categoryEntity.setNameUz(categoryDTO.getNameUz());
         categoryEntity.setNameRu(categoryDTO.getNameRu());
         categoryEntity.setPrtId(prtId);
         categoryRepository.save(categoryEntity);
         return "Category is updated";
    }
    //3 delete by admin
    @CacheEvict(value = "category",key = "#id")
    public String deleteCategory(Integer id){
        return categoryRepository.deleteCategory(id)>0?"category deleted":"category not deleted";
    }
    //4 get all admin
    @Cacheable(value = "category")
    public List<CategoryDTO>getAll(){
     return categoryRepository.findAllByVisibleTrueOrderByOrderNumberAsc().stream().map(s->toDto(s)).toList();
    }
    //5 get by language
    @Cacheable(value = "category",key = "language")
    public List<CategoryDTO>getByLang(Language language){
        List<CategoryLanguageMapper>mapperList=categoryRepository.getAllByLang(language.name().toLowerCase());
        List<CategoryDTO> dtoList=new LinkedList<>();
        mapperList.forEach(s->{
            CategoryDTO categoryDTO=new CategoryDTO();
            categoryDTO.setId(s.getId());
            categoryDTO.setOrderNumber(s.getOrderNumber());
            categoryDTO.setName(s.getName());
            dtoList.add(categoryDTO);
        });
        System.out.println(dtoList);
        return dtoList;
    }
    private CategoryEntity toEntity(CategoryDTO categoryDTO){
        CategoryEntity categoryEntity=new CategoryEntity();
        categoryEntity.setOrderNumber(categoryDTO.getOrderNumber());
        categoryEntity.setNameEn(categoryDTO.getNameEn());
        categoryEntity.setNameUz(categoryDTO.getNameUz());
        categoryEntity.setNameRu(categoryDTO.getNameRu());
        return categoryEntity;
    }
    private CategoryDTO toDto(CategoryEntity categoryEntity){
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setOrderNumber(categoryEntity.getOrderNumber());
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        categoryDTO.setNameEn(categoryEntity.getNameEn());
        categoryDTO.setNameUz(categoryEntity.getNameUz());
        categoryDTO.setNameRu(categoryEntity.getNameRu());
        categoryDTO.setVisible(categoryEntity.getVisible());
        categoryDTO.setPrtId(categoryEntity.getPrtId());
        return categoryDTO;
    }

    private CategoryEntity getCategoryEntity(Integer categoryId){
        return categoryRepository.findByIdAndVisibleTrue(categoryId)
                .orElseThrow(()->new ItemNotFoundException("Category not found"));
    }

}
