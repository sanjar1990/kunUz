package com.example.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public interface ArticleShortInfoMapper {
    //id(uuid),title,description,image(id,url),published_date
    String getArticleId();
    String getTitle();
    String getDescription();
    String getImageId();
    String getImageUrl();
    LocalDateTime getPublishedDate();
}
