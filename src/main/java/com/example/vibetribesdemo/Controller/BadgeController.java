package com.example.vibetribesdemo.Controller;

import com.example.vibetribesdemo.DTOs.BadgeRequestDto;
import com.example.vibetribesdemo.DTOs.BadgeResponseDto;
import com.example.vibetribesdemo.DTOs.UserBadgeResponseDto;
import com.example.vibetribesdemo.Service.BadgeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping
    public ResponseEntity<List<BadgeResponseDto>> getAllBadges() {
        return ResponseEntity.ok(badgeService.getAllBadges());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<UserBadgeResponseDto>> getUserBadges(@PathVariable String username) {
        return ResponseEntity.ok(badgeService.getUserBadges(username));
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateBadge(@RequestBody @Valid BadgeRequestDto badgeRequestDto) {
        badgeService.createOrUpdateBadge(badgeRequestDto);
        return ResponseEntity.ok("Badge created/updated successfully.");
    }

    @DeleteMapping("/{badgeId}")
    public ResponseEntity<?> deleteBadge(@PathVariable Long badgeId) {
        badgeService.deleteBadge(badgeId);
        return ResponseEntity.ok("Badge deleted successfully.");
    }
}


