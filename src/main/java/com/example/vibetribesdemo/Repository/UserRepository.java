package com.example.vibetribesdemo.Repository;

import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Method to find a user by email (used during login and JWT processing)

    Optional<UserEntity> findByUsername(String username);


}
