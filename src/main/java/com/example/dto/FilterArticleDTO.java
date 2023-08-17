package com.example.dto;

import com.example.enums.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterArticleDTO {
    private String title;
    private Integer regionId;
    private Integer categoryId;
    private LocalDate cratedDateFrom;
    private LocalDate createdDateTo;
    private LocalDate publishedDateFrom;
    private LocalDate publishedDateTo;
    private Integer moderatorId;
    private Integer publisherId;
    private ArticleStatus status;
}
