package com.example.vibetribesdemo.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequestDto {
    @NotBlank
    @Size(max = 100, message = "Event title must be at most 100 characters")
    private String title;

    @Size(max = 500, message = "Event description must be at most 500 characters")
    private String description;

    @NotNull(message = "Location ID is required")
    private Long locationId; // Reference to a location entity

    @NotNull(message = "Start time cannot be null")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalDateTime endTime;

    @Min(1)
    private Integer maxAttendees;


}
