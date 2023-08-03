package com.example.dto;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotNull(message = "name is required")
    @Size(min = 3, message = "name character should be more then 3")
    private String name;
    @NotNull(message = "surname is required")
    @Size(min = 3, message = " surname character should be more then 3")
    private String surname;
    @Email(message = "incorrect email format")
    private String email;
    @NotNull(message = "name is required")
    private String phone;
    @NotNull(message = "password is required")
    @Size(min = 5, message = "password character should be more then 5")
    private String password;
    private ProfileStatus status;
    @NotNull(message = "role is required")
    private ProfileRole role;
    private Boolean visible;
    private LocalDateTime createdDate;
    private String photoId;
    private Integer prtId;
    private String jwt;
}
