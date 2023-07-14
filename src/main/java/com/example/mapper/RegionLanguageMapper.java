package com.example.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegionLanguageMapper {
    private Integer id;
    private Integer orderNumber;
    private String regionName;

    public RegionLanguageMapper(Integer id, Integer orderNumber, String regionName) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.regionName = regionName;
    }
}
