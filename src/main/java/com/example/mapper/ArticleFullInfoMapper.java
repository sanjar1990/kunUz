package com.example.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFullInfoMapper {
    private String id;
    private String title;
    private String description;
    private String content;
    private String imageId;
    private Integer sharedCount;
    private Integer regionOrderNumber;
    private String regionName;
    private Integer categoryOrderNumber;
    private String categoryName;
    private LocalDateTime publishedDate;
    private Integer viewCount;
    private Integer likeCount;
    private List<String>tagNameList;

}
