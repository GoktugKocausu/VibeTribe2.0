package com.example.vibetribesdemo.ServiceImplementation.User;

import com.example.vibetribesdemo.DTOs.User.UserProfileUpdateDto;
import com.example.vibetribesdemo.Repository.User.UserRepository;
import com.example.vibetribesdemo.Service.User.UserService;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${profile.pictures.directory}")
    private String profilePicturesDirectory;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity updateUserProfile(String username, UserProfileUpdateDto userProfileUpdateDto) {
        // Find the user by email (username is the email in this case)
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update profile details if provided in the DTO
        if (userProfileUpdateDto.getBio() != null) {
            user.setBio(userProfileUpdateDto.getBio());
        }
        if (userProfileUpdateDto.getAge() != null) {
            user.setAge(userProfileUpdateDto.getAge());
        }

        user.setLastLogin(LocalDateTime.now()); // Optional: Update last login time
        userRepository.save(user); // Save the updated user entity to the database
        return user;
    }

    @Override
    public String uploadProfilePicture(String username, MultipartFile file) {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            String filename = username + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(profilePicturesDirectory, filename);
            Files.write(filePath, file.getBytes());
            user.setProfilePicture(filePath.toString());
            userRepository.save(user); // Save the file path in the database
            return "Profile picture uploaded successfully";
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }
}
