package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO {
   private Integer id;
   private Integer orderNumber;
   private String nameUz;
   private String nameRu;
   private String nameEng;
   private Boolean visible;
   private LocalDateTime createdDate;

}
