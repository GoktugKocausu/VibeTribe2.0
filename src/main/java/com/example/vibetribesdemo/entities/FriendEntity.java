package com.example.vibetribesdemo.entities;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friends")
@Data
public class FriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendsId; // Unique identifier for friend associations

    @ManyToOne // Relationship with User
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // User who has friends

    @ManyToOne // Relationship with User
    @JoinColumn(name = "friend_id", nullable = false)
    private UserEntity friend; // Friend of the user

    @NotBlank
    private String status; // Status of the friendship (e.g., pending, accepted, blocked)

    private LocalDateTime createdAt; // Timestamp when the friendship was created

    private LocalDateTime acceptedAt; // Timestamp when the friendship was accepted (if applicable)

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Set the created timestamp
    }
}
