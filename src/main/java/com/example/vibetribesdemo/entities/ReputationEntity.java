package com.example.vibetribesdemo.entities;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reputation_history")
@Data
public class ReputationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reputationId; // Unique identifier for each reputation record

    @Min(1)
    @Max(10)
    private Integer points;

    @ManyToOne // Relationship with User
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // User whose reputation is being recorded

    @ManyToOne // Relationship with Event (optional)
    @JoinColumn(name = "event_id")
    private EventEntity event; // Event associated with the reputation change (optional)

    @ManyToOne // Relationship with User who awarded points
    @JoinColumn(name = "awarded_by", nullable = false)
    private UserEntity awardedBy; // User who awarded the reputation points

    @Min(1)
    private Integer changeAmount; // Amount of reputation points changed

    @Size(max = 255)
    private String reason; // Reason for the reputation change

    private String eventType; // Type of event associated with reputation change (if relevant)

    private LocalDateTime timestamp; // Timestamp of when the reputation change occurred

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now(); // Set the timestamp when a new reputation record is created
    }
}
