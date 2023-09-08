package com.example.repository;

import com.example.entity.ArticleEntity;
import com.example.enums.ArticleStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void itShouldCreateArticle() {
        // given
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle("test article");
        articleEntity.setContent("content");
        articleEntity.setDescription("description");
        articleEntity.setStatus(ArticleStatus.PUBLISHED);
        articleEntity.setVisible(true);
        articleEntity.setImageId(UUID.randomUUID().toString());
        // when
        articleRepository.save(articleEntity);
        System.out.println(articleEntity.getId());
        // then
        System.out.println("aaaa");
        Optional<ArticleEntity> optional = articleRepository.findById(articleEntity.getId());
        if(optional.isPresent()){
            System.out.println("optional present");
        }
//        Assertions.assertThat(optional.isPresent()).isTrue();
    }

}