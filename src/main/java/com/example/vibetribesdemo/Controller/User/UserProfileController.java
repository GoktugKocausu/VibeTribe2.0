package com.example.vibetribesdemo.Controller.User;

import com.example.vibetribesdemo.DTOs.UserProfileUpdateDto;
import com.example.vibetribesdemo.Service.User.UserService;
import com.example.vibetribesdemo.entities.UserEntity;
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
    public List<UserEntity> searchUsers(@RequestParam String query, @RequestParam String currentUsername) {
        UserEntity currentUser = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return userService.searchUsers(query, currentUser);
    }

}