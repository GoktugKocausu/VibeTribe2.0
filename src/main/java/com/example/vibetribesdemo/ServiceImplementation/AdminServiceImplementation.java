package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.DTOs.User.UserDto;
import com.example.vibetribesdemo.Service.AdminService;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
