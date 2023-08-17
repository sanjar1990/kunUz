package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private String id;
    private Integer profileId;
    private ProfileDTO profileDTO;
    @NotBlank(message = "content is required")
    private String content;
    @NotBlank(message = "articleId is required")
    private String articleId;
    private ArticleDTO articleDTO;
    private String replyId;
    private Boolean visible;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    }
