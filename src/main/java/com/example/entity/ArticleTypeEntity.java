package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "article_type")
public class ArticleTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(name = "orderNumber")
    private Integer orderNumber;
    @Column(name = "name_uz")
    private String nameUz;
    @Column(name = "name_ru")
    private String nameRu;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "visible")
    private Boolean visible=true;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
}