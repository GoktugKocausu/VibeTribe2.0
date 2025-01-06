package com.example.vibetribesdemo.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BadgeRequestDto {
    @NotBlank
    @Size(max = 100)
    private String badgeName;

    @Size(max = 255)
    private String description;

    @Min(0)
    private Integer reputationRequirement;

    @Min(0)
    private Integer eventCountRequirement;
}
