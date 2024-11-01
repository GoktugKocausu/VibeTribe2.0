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

    private Double latitude; // Latitude for mapping purposes

    private Double longitude; // Longitude for mapping purposes

    @NotBlank
    private String type; // Type of location (e.g., indoor, outdoor)

    @Min(0)
    private Integer currentCapacity; // Current number of people at the location

    @Min(1)
    private Integer maxCapacity; // Maximum capacity of the location

    // Optionally, you can add timestamps for createdAt and updatedAt if needed.
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
