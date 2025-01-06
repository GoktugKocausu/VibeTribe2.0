package com.example.vibetribesdemo.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserBadgeResponseDto {
    private String badgeName;
    private String description;
    private LocalDateTime earnedAt;
}
