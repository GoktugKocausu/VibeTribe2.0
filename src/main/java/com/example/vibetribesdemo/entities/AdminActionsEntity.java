package com.example.vibetribesdemo.entities;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_actions")
@Data
public class AdminActionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminActionId; // Unique identifier for admin actions

    @ManyToOne // Relationship with Admin (User)
    @JoinColumn(name = "admin_id", nullable = false)
    private UserEntity admin; // Admin who performed the action

    @ManyToOne // Relationship with Target User (User)
    @JoinColumn(name = "target_user_id", nullable = false)
    private UserEntity targetUser; // User affected by the action

    @NotBlank
    private String actionType; // Type of action taken (e.g., ban, suspension)

    @Size(max = 255)
    private String reason; // Reason for the admin action

    private LocalDateTime timestamp; // Timestamp when the action was taken

    private Integer duration; // Duration for temporary actions (if applicable)

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now(); // Set the timestamp when a new admin action is created
    }
}
