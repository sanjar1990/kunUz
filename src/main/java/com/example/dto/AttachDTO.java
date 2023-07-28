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
public class AttachDTO {
    private String id;
    private String originalName;
    private String path;
    private Long size;
    private String extension;
    private LocalDateTime createdDate;
    private String url;
}
