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
@Table(name = "category")
public class CategoryEntity extends BaseEntity{

    @Column(name = "order_number",unique = true)
    private Integer orderNumber;
    @Column(name = "name_uz",nullable = false)
    private String nameUz;
    @Column(name = "name_ru",nullable = false)
    private String nameRu;
    @Column(name = "name_en",nullable = false)
    private String nameEn;
}
