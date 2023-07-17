package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDTO {
   private Integer id;
   private Integer orderNumber;
   private String nameUz;
   private String nameRu;
   private String nameEn;
   private Boolean visible;
   private LocalDateTime createdDate;
   private String regionName;

}
