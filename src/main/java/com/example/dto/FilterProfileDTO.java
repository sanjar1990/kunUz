package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterProfileDTO {
    private String name;
    private String surname;
    private String phone;
    private ProfileRole role;
    private LocalDate created_date_from;
    private LocalDate created_date_to;
}
