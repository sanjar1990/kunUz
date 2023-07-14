package com.example.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryLanguageMapper {
    private Integer id;
    private Integer orderNumber;
    private String categoryName;

    public CategoryLanguageMapper(Integer id, Integer orderNumber, String categoryName) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.categoryName = categoryName;
    }
}
