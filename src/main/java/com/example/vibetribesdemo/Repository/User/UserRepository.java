package com.example.vibetribesdemo.Repository.User;

import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Method to find a user by email (used during login and JWT processing)
    Optional<UserEntity> findByEmail(String email);

    // Method to check if a username already exists (useful for validation)
    boolean existsByUsername(String username);

    // Method to check if an email already exists (useful for validation)
    boolean existsByEmail(String email);
}
