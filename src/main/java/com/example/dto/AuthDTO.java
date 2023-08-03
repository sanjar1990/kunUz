package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    @NotBlank(message = "phone is required")
    private String phone;
    @NotBlank(message = "Password is required")
    @Size(min = 5,message = "length should be greater than 5")
    private String password;
}
