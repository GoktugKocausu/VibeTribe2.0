package com.example.vibetribesdemo.entities;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "badges")
@Data
public class BadgesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId; // Unique identifier for each badge

    @NotBlank
    @Size(max = 100)
    private String badgeName; // Name of the badge

    @Size(max = 255)
    private String description; // Description of the badge and its significance

    @Min(0)
    private Integer reputationRequirement; // Number of reputation points needed to earn the badge

    @Min(0)
    private Integer eventCountRequirement; // Number of events participated in to earn the badge

    // Optionally, you can add timestamps for createdAt and updatedAt if needed.
    private LocalDateTime createdAt; // Timestamp when the badge was created

    private LocalDateTime updatedAt; // Timestamp when the badge was last modified

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Set the created timestamp
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(); // Set the updated timestamp
    }
}
