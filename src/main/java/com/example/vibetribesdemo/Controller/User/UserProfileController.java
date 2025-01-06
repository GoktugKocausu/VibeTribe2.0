package com.example.vibetribesdemo.Controller.User;

import com.example.vibetribesdemo.DTOs.UserProfileUpdateDto;
import com.example.vibetribesdemo.Service.User.UserService;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update")
    public ResponseEntity<UserEntity> updateUserProfile(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestBody UserProfileUpdateDto userProfileUpdateDto) {
        System.out.println("Profile update request received for user: " + username);
        UserEntity updatedUser = userService.updateUserProfile(username, userProfileUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }




    @PostMapping("/upload-picture")
    public ResponseEntity<String> uploadProfilePicture(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestParam("file") MultipartFile file) {
        String message = userService.uploadProfilePicture(username, file);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/search")
    public Page<UserEntity> searchUsers(
            @RequestParam String query,
            @RequestParam String currentUsername,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        UserEntity currentUser = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Sort sort = Sort.by(sortBy);
        sort = "desc".equalsIgnoreCase(sortDirection) ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return userService.searchUsers(query, currentUser, pageable);
    }


    @GetMapping("/{username}/hosted-events/count")
    public ResponseEntity<Integer> getHostedEventsCount(@PathVariable String username) {
        int count = userService.getHostedEventsCount(username);
        return ResponseEntity.ok(count);
    }

}