package com.example.dto;

import com.example.entity.ProfileEntity;
import com.example.enums.CommentLikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLikeDTO {
    private String id;
    private Integer profileId;
    private String commentId;
    private CommentLikeStatus status;
    private LocalDateTime createdDate;
}
