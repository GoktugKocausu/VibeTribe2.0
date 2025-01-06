package com.example.vibetribesdemo.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequestDto {
    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Min(1)
    private Integer maxAttendees;
}
