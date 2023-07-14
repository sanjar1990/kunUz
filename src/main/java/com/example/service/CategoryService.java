package com.example.service;

import com.example.dto.CategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemAlreadyExists;
import com.example.exception.ItemNotFoundException;
import com.example.mapper.CategoryLanguageMapper;
import com.example.repository.CategoryRepository;
import com.example.utility.CheckValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CheckValidationUtility checkValidationUtility;
    // 1 create by admin
    public CategoryDTO createCategory(CategoryDTO categoryDTO){
        checkValidationUtility.checkCategory(categoryDTO);
        Optional<CategoryEntity> optional=categoryRepository
                .findByNameEngOrNameUzOrNameRu(categoryDTO.getNameEng(),categoryDTO.getNameUz(), categoryDTO.getNameRu());
        if(optional.isPresent())throw new ItemAlreadyExists("This category is exists");
        Optional<CategoryEntity>byOrderNum=categoryRepository.findByOrderNumber(categoryDTO.getOrderNumber());
        if (byOrderNum.isPresent())throw new ItemAlreadyExists("This order num is exists");
        CategoryEntity categoryEntity=toEntity(categoryDTO);
        categoryRepository.save(categoryEntity);
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        categoryDTO.setVisible(categoryEntity.getVisible());
        return categoryDTO;
    }
    //2 update by admin
    public String updateCategory(CategoryDTO categoryDTO, Integer id){
        Optional<CategoryEntity> optional=categoryRepository.findById(id);
        Optional<CategoryEntity> isExists=categoryRepository
                .findByNameEngOrNameUzOrNameRu(categoryDTO.getNameEng(),categoryDTO.getNameUz(), categoryDTO.getNameRu());
        if(optional.isEmpty()){
            throw new ItemNotFoundException("Category not found!");
        }
        if(isExists.isPresent()) throw new ItemAlreadyExists("this category already exists");
        CategoryEntity categoryEntity=optional.get();

       if(categoryDTO.getOrderNumber()!=null){
         categoryEntity.setOrderNumber(categoryDTO.getOrderNumber());
       }
       if(categoryDTO.getNameEng()!=null){
           categoryEntity.setNameEng(categoryDTO.getNameEng());
       }
       if(categoryDTO.getNameUz()!=null){
           categoryEntity.setNameUz(categoryDTO.getNameUz());
       }
       if(categoryDTO.getNameRu()!=null){
           categoryEntity.setNameRu(categoryDTO.getNameRu());
       }
       categoryRepository.save(categoryEntity);
       return "Category is updated";
    }
    //3 delete by admin
    public String deleteCategory(Integer id){
        Optional<CategoryEntity> optional=categoryRepository.findById(id);
        if(optional.isEmpty()){
            throw new ItemNotFoundException("Category not found!");
        }
        CategoryEntity categoryEntity=optional.get();
        if(!categoryEntity.getVisible()){
            throw new AppBadRequestException("this category already deleted");
        }
        int result= categoryRepository.deleteCategory(id);
        return result>0?"category deleted":"category not deleted";
    }
    //4 get all admin
    public List<CategoryDTO>getAll(){
        Iterable<CategoryEntity> iterable=categoryRepository.findAll();
        List<CategoryDTO> dtoList=new LinkedList<>();
        iterable.forEach(s->dtoList.add(toDto(s)));
        return dtoList;
    }
    //5 get by language
    public List<CategoryLanguageMapper>getByLang(String language){
        if(language==null) throw new AppBadRequestException("Enter language!");
        if(language.toLowerCase().startsWith("eng")){
            return categoryRepository.getByEnglish();
        } else if (language.toLowerCase().startsWith("uz")) {
            return categoryRepository.getByUzbek();
        }else if(language.toLowerCase().startsWith("ru")){
            return categoryRepository.getByRussian();
        }
        return null;
    }
    private CategoryEntity toEntity(CategoryDTO categoryDTO){
        CategoryEntity categoryEntity=new CategoryEntity();
        categoryEntity.setOrderNumber(categoryDTO.getOrderNumber());
        categoryEntity.setNameEng(categoryDTO.getNameEng());
        categoryEntity.setNameUz(categoryDTO.getNameUz());
        categoryEntity.setNameRu(categoryDTO.getNameRu());
        return categoryEntity;
    }
    private CategoryDTO toDto(CategoryEntity categoryEntity){
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setOrderNumber(categoryEntity.getOrderNumber());
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        categoryDTO.setNameEng(categoryEntity.getNameEng());
        categoryDTO.setNameUz(categoryEntity.getNameUz());
        categoryDTO.setNameRu(categoryEntity.getNameRu());
        categoryDTO.setVisible(categoryEntity.getVisible());
        return categoryDTO;
    }

}
