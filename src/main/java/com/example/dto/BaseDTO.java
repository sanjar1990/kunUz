package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseDTO {
    private String name;
    private Integer id;
    @NotNull(message = "order number is required")
    private Integer orderNumber;
    @NotBlank(message = "name uz is required")
    private String nameUz;
    @NotBlank(message = "name ru is required")
    private String nameRu;
    @NotBlank(message = "name en is required")
    private String nameEn;
    private Boolean visible;
    private LocalDateTime createdDate;
    private Integer prtId;
}
