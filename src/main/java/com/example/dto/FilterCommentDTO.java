package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterCommentDTO {
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
    private Integer profileId;
    private String articleId;
}
