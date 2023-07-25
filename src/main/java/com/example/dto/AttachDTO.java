package com.example.dto;

<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
>>>>>>> attachPractise
public class AttachDTO {
    private String id;
    private String originalName;
    private String path;
    private Long size;
    private String extension;
<<<<<<< HEAD
    private LocalDateTime createdData;
    private String url;
=======
    private LocalDateTime createdDate;
>>>>>>> attachPractise
}
