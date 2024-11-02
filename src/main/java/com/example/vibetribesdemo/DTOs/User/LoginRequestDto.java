package com.example.vibetribesdemo.DTOs.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank
    private String password;
}
