package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String content;
    private String articleId;
    private ArticleDTO articleDTO;
    private String replyId;
    private Boolean visible;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    }
