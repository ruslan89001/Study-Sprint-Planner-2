package com.studysprint.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email must not be blank")
    private String email;
}
