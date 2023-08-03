package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileUpdateDetailDTO {
    @NotNull(message = "name is required")
    @Size(min = 3, message = "name character should be more then 3")
    private String name;
    @NotNull(message = "surname is required")
    @Size(min = 3, message = " surname character should be more then 3")
    private String surname;

}
