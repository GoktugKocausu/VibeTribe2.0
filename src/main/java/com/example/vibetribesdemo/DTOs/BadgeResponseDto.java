package com.example.vibetribesdemo.DTOs;

import lombok.Data;

@Data
public class BadgeResponseDto {
    private Long badgeId;
    private String badgeName;
    private String description;
    private Integer reputationRequirement;
    private Integer eventCountRequirement;
}

