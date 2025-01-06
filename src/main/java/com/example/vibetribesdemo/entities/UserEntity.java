package com.example.vibetribesdemo.entities;

import com.example.vibetribesdemo.Utilities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Data
@ToString(exclude = "hostedEvents")
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

    @Column(nullable = false)
    private Integer reputationPoints = 0; // Initialize with 0

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EventEntity> hostedEvents; // Events hosted by this user

    public int getHostedEventsCount() {
        return hostedEvents != null ? hostedEvents.size() : 0;
    }

    public int getReputationPointsCount() {
        return reputationPoints != null ? reputationPoints : 0;
    }



    private String preferredMood;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @NotNull(message = "Status cannot be null")
    private String status;

    @NotNull
    private String sex;

    private Integer age;
    // New fields (nullable)
    private String name;
    private String surname;


    @Size(max = 15, message = "Phone number must be at most 15 characters long")
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+[0-9]+$",
            message = "Phone number must start with '+' and contain only digits"
    )
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER_ROLE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return userId != null && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return this.username;
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
