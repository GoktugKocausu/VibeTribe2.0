package com.example.vibetribesdemo.entities;

import com.example.vibetribesdemo.Utilities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Data
public class UserEntity implements UserDetails {

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

    @NotNull(message = "Status cannot be null")
    private String status; // or set an appropriate default value


    @NotNull
    private String sex;

    private Integer age;

    @Size(max = 15, message = "Phone number must be at most 15 characters long")
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+[0-9]+$", // Phone number must start with '+' followed by digits
            message = "Phone number must start with '+' and contain only digits"
    )
    private String phoneNumber;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    // Implement UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // You may need to define authorities later if required
    }
    @Override
    public String getUsername() {
        return this.email; // Return email as username
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
