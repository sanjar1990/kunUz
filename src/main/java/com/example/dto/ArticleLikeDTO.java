package com.example.dto;

import com.example.enums.ArticleLiceStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleLikeDTO{
   private String id;
   private Integer profileId;
   private String articleId;
   private ArticleLiceStatus status;
   private LocalDateTime createdDate;
}
