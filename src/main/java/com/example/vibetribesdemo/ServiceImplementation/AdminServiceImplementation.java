package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.DTOs.UserDto;
import com.example.vibetribesdemo.Service.AdminService;
import com.example.vibetribesdemo.Utilities.Role;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImplementation implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> banUser(Long userId) {
        return updateStatus(userId, "BANNED");
    }



    @Override
    public ResponseEntity<?> unbanUser(Long userId) {
        return updateStatus(userId, "ACTIVE");
    }

    private ResponseEntity<?> updateStatus(Long userId, String status) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setStatus(status);  // Update the status
                    userRepository.save(user);
                    return ResponseEntity.ok("User status updated to " + status);
                })
                .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
    }

    @Override
    public ResponseEntity<?> promoteToAdmin(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setRole(Role.ADMIN_ROLE);  // Set role to ADMIN_ROLE
                    userRepository.save(user);  // Save changes
                    return ResponseEntity.ok("User promoted to admin");
                })
                .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
    }


    // New method to unpromote a user to USER_ROLE
    public ResponseEntity<?> unpromoteToUser(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setRole(Role.USER_ROLE); // Set role back to USER_ROLE
            userRepository.save(user);
            return ResponseEntity.ok("User demoted to USER_ROLE successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}
