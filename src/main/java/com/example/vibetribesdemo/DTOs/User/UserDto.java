package com.example.vibetribesdemo.DTOs.User;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserDto {

    private Long userId;
    private String username;
    private String email;
    private String profilePicture;
    private String bio;
    private Integer reputationPoints;
    private String preferredMood;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String status;
}
