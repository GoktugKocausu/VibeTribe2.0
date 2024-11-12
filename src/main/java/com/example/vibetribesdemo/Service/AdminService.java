package com.example.vibetribesdemo.Service;

import com.example.vibetribesdemo.DTOs.UserDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers();
    ResponseEntity<?> banUser(Long userId);    // Ban a user
    ResponseEntity<?> unbanUser(Long userId);  // Unban a user

    ResponseEntity<?> promoteToAdmin(Long userId);

    ResponseEntity<?> unpromoteToUser(Long userId);
}


