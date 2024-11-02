package com.example.vibetribesdemo.entities;


import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Data
public class AttandanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId; // Unique identifier for each attendance record

    @ManyToOne // Relationship with User
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // User attending the event

    @ManyToOne // Relationship with Event
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event; // Event being attended

    @NotBlank
    private String status; // Status of attendance (e.g., RSVP’d, Attended, Cancelled)

    private LocalDateTime timestamp; // Timestamp of when the attendance was recorded

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now(); // Set the timestamp when a new attendance record is created
    }

}
