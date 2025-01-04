package com.example.vibetribesdemo.Controller;

import com.example.vibetribesdemo.DTOs.EventResponseDto;
import com.example.vibetribesdemo.DTOs.UserDto;
import com.example.vibetribesdemo.Service.AdminService;
import com.example.vibetribesdemo.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final EventService eventService;

    @Autowired
    public AdminController(AdminService adminService, EventService eventService) {
        this.adminService = adminService;
        this.eventService = eventService;
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

    @GetMapping("/promote/{userId}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long userId) {
        return adminService.promoteToAdmin(userId);
    }

    @GetMapping("/unpromote/{userId}")
    public ResponseEntity<?> unpromoteToUser(@PathVariable Long userId) {
        return adminService.unpromoteToUser(userId);
    }

    // Admin endpoint to view all events
    @GetMapping("/events")
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Admin endpoint to cancel an event
    @PutMapping("/events/{eventId}/cancel")
    public ResponseEntity<String> cancelEvent(@PathVariable Long eventId) {
        eventService.cancelEventByAdmin(eventId);
        return ResponseEntity.ok("Event canceled successfully.");
    }
}