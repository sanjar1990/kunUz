package com.example.dto;

import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.RegionEntity;
import com.example.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private String id;
    @NotBlank(message = "title is required")
    @Size(min = 3)
    private String title;
    @NotBlank(message = "description is required")
    private String description;
    @NotBlank(message = "content is required")
    private String content;
    private Integer sharedCount;
    @NotBlank(message = "image id is required")
    private String imageId;
    private AttachDTO image;
    @NotNull(message = "region id is required")
    private Integer regionId;
    private RegionDTO regionDTO;
    @NotNull(message = "category id is required")
    private Integer categoryId;
    private CategoryDTO categoryDTO;
    private Integer moderatorId;
    private Integer publisherId;
    private ArticleStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private boolean visible;
    private Integer viewCount;
    @NotEmpty(message = "articleTypes are required")
    private List<Integer> articleTypes;
    private List<Integer>tagList;
    private List<TagDTO> tagDTOList;
}
