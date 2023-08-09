package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class JwtDTO {
    private Integer id;
    private ProfileRole role;
    private String phone;
    public JwtDTO(Integer id) {
        this.id = id;
    }
    public JwtDTO( String phone,ProfileRole role) {
        this.phone = phone;
        this.role = role;
    }
}
