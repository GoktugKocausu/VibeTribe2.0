package com.example.vibetribesdemo.entities;

import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "notifications")
@Data
public class NotificationsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId; // Unique identifier for each notification

    @ManyToOne // Relationship with User
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // User receiving the notification

    @NotBlank
    @Size(max = 500) // Adjust the size as necessary for your use case
    private String content; // Content of the notification

    private Boolean readStatus; // Status indicating if the notification has been read

    @NotBlank
    private String type; // Type of notification (e.g., event updates, direct messages)

    private LocalDateTime timestamp; // Timestamp when the notification was generated

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now(); // Set the timestamp when a new notification is created
    }
}
