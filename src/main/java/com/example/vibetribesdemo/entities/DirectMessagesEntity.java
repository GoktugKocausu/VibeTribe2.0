package com.example.vibetribesdemo.entities;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "direct_messages")
@Data
public class DirectMessagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId; // Unique identifier for each direct message

    @ManyToOne // Relationship with Sender (User)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender; // User sending the message

    @ManyToOne // Relationship with Receiver (User)
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserEntity receiver; // User receiving the message

    @NotBlank
    @Size(max = 500) // Adjust the size as necessary for your use case
    private String content; // Content of the message

    private LocalDateTime timestamp; // Timestamp when the message was sent

    private Boolean isRead; // Boolean indicating if the message has been read

    @NotBlank
    private String status; // Status of the message (e.g., sent, delivered, read)

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now(); // Set the timestamp when a new message is created
    }
}
