package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotBlank(message = "name is required!")
    @Size(min = 3, message = "name character should not be less than 3")
    private String name;
    @NotBlank(message = "surname is required!")
    @Size(min = 3, message = "surname character should not be less than 3")
    private String surname;
    @Email(message = "invalid email format")
    private String email;
    @NotBlank(message = "name is required!")
    private String phone;
    @NotBlank(message = "name is required!")
    @Size(min = 5, message = "password character should not be less than 5")
    private String password;
    }
