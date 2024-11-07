package com.example.vibetribesdemo.DTOs.User;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserProfileUpdateDto {
    @Size(max = 255, message = "Bio must be at most 255 characters long")
    private String bio;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;

    private MultipartFile profilePicture; // Field for file uploads if needed
}