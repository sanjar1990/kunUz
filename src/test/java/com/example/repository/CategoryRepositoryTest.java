package com.example.repository;

import com.example.entity.CategoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void itShouldCreateCategory(){
        //give
        CategoryEntity entity= new CategoryEntity();
        entity.setNameUz("test_category_uz");
        entity.setNameEn("test_category_En");
        entity.setNameRu("test_category_RU");
        entity.setPrtId(1);
        //when
        categoryRepository.save(entity);
        Optional<CategoryEntity>optional=categoryRepository.findById(entity.getId());
        //then
        Assertions.assertTrue(optional.isPresent(),"assertion created");
    }
    @Test
    public void itShouldNotCreateCategory(){
        //give
        CategoryEntity entity= new CategoryEntity();
        entity.setNameUz(null);
        entity.setPrtId(1);
        //when

       Assertions.assertThrows(RuntimeException.class,()->{categoryRepository.save(entity);}).fillInStackTrace();
    }
    @Test
    public void itShouldNotFindCategory(){
        Optional<CategoryEntity>optional=categoryRepository.findById(3);
        Assertions.assertTrue(optional.isEmpty());
    }
    @Test
    public void itShouldFindCategoryByNameUz(){
        CategoryEntity entity= new CategoryEntity();
        entity.setNameUz("test_category_uz");
        entity.setNameEn("test_category_En");
        entity.setNameRu("test_category_RU");
        entity.setPrtId(1);
        categoryRepository.save(entity);
        Optional<CategoryEntity> optional=categoryRepository.findByNameUz(entity.getNameUz());
        Assertions.assertTrue(optional.isPresent());
    }
}
