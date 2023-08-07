package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavedArticleDTO {
    private String id;
    @NotNull(message = "profile id is required")
    private Integer profileId;
    @NotBlank(message = "article id is required")
    private String articleId;
    private ArticleDTO article;
    private LocalDateTime createdDate;
}
