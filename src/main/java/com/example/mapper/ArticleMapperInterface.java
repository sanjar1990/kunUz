package com.example.mapper;

import com.example.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface ArticleMapperInterface {
    String getId();
    String getTitle();
    String getDescription();
    String getContent();
    String getImageId();
    Integer getSharedCount();
    Integer getRegionId();
    Integer getCategoryId();
    Integer getModeratorId();
    Integer getPublisherId();
    String getStatus();
    LocalDateTime getCreatedDate();
    LocalDateTime getPublishedDate();
    Boolean getVisible();
    Integer getViewCount();
}
