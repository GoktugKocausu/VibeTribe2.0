package com.example.vibetribesdemo.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponseDto {
    private Long eventId;
    private String title;
    private String description;
    private String createdBy; // Username of the creator
    private String locationName; // Name of the location
    private Long locationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxAttendees;
    private Integer currentAttendees;
    private String status; // ACTIVE, CANCELED, etc.
}
