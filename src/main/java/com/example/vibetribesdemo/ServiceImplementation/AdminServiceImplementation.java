package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.DTOs.UserDto;
import com.example.vibetribesdemo.Service.AdminService;
import com.example.vibetribesdemo.Service.EventService;
import com.example.vibetribesdemo.Utilities.Role;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.vibetribesdemo.DTOs.EventResponseDto;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImplementation implements AdminService {

    private final UserRepository userRepository;
    private final EventService eventService;

    @Autowired
    public AdminServiceImplementation(UserRepository userRepository, EventService eventService) {
        this.userRepository = userRepository;
        this.eventService = eventService; // Inject EventService
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
                    user.setStatus(status);
                    userRepository.save(user);
                    return ResponseEntity.ok("User status updated to " + status);
                })
                .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
    }

    @Override
    public ResponseEntity<?> promoteToAdmin(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setRole(Role.ADMIN_ROLE);
                    userRepository.save(user);
                    return ResponseEntity.ok("User promoted to admin");
                })
                .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
    }

    @Override
    public ResponseEntity<?> unpromoteToUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setRole(Role.USER_ROLE);
                    userRepository.save(user);
                    return ResponseEntity.ok("User demoted to USER_ROLE successfully.");
                })
                .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
    }

    // New method: Get all events
    @Override
    public List<EventResponseDto> getAllEvents() {
        return eventService.getAllEvents();
    }

    // New method: Cancel an event by admin
    @Override
    public void cancelEventByAdmin(Long eventId) {
        eventService.cancelEventByAdmin(eventId);
    }
}

