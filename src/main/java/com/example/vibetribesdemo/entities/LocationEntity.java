package com.example.vibetribesdemo.entities;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@Data
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId; // Unique identifier for each location

    @NotBlank
    @Size(max = 100)
    private String name; // Name of the location (e.g., coffee shop)

    @NotBlank
    @Size(max = 255)
    private String address; // Physical address of the location

    @Size(max = 50)
    private String city; // City where the location is

    @Size(max = 50)
    private String country; // Country of the location

    private Double latitude; // Latitude for mapping purposes

    private Double longitude; // Longitude for mapping purposes

    @NotBlank
    @Size(max = 20)
    private String type; // Type of location (e.g., indoor, outdoor)

    @Min(0)
    private Integer currentCapacity = 0; // Default to 0

    @Min(1)
    private Integer maxCapacity; // Maximum capacity of the location

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Timestamp when the location was created

    private LocalDateTime updatedAt; // Timestamp when the location was last modified

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
