package com.example.vibetribesdemo.entities;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(
            regexp = "^(?![_.])[a-zA-Z0-9._]{3,20}(?<![_.])$",
            message = "Username must be 3-20 characters long, can contain letters, numbers, dots, and underscores, but cannot start or end with a dot or underscore."
    )
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String passwordHash;

    private String profilePicture;

    @Size(max = 255)
    private String bio;

    @Min(0)
    private Integer reputationPoints;

    private String preferredMood;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @NotNull
    private String status;

    // Define relationships here as needed, e.g., @OneToMany for events created by this user.
}
