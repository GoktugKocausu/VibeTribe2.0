package com.example.vibetribesdemo.Controller;

import com.example.vibetribesdemo.DTOs.User.UserDto;
import com.example.vibetribesdemo.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/ban/{userId}")
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        return adminService.banUser(userId);
    }

    @PostMapping("/unban/{userId}")
    public ResponseEntity<?> unbanUser(@PathVariable Long userId) {
        return adminService.unbanUser(userId);
    }
    // New endpoint to promote a user to ADMIN_ROLE
    @GetMapping("/promote/{userId}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long userId) {
        return adminService.promoteToAdmin(userId);
    }

    // New endpoint to demote a user to USER_ROLE
    @GetMapping("/unpromote/{userId}")
    public ResponseEntity<?> unpromoteToUser(@PathVariable Long userId) {
        return adminService.unpromoteToUser(userId);
    }
}
