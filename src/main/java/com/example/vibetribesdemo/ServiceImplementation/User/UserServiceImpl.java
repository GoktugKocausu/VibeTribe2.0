package com.example.vibetribesdemo.ServiceImplementation.User;

import com.example.vibetribesdemo.DTOs.UserProfileUpdateDto;
import com.example.vibetribesdemo.Repository.UserRepository;
import com.example.vibetribesdemo.Service.User.UserService;
import com.example.vibetribesdemo.entities.UserEntity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${profile.pictures.directory}")
    private String profilePicturesDirectory;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity updateUserProfile(String username, UserProfileUpdateDto userProfileUpdateDto) {

        UserEntity user = userRepository.findByUsername(username)
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
        UserEntity user = userRepository.findByUsername(username)
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

    @Override
    public Page<UserEntity> searchUsers(String query, UserEntity currentUser, Pageable pageable) {
        return userRepository.searchUsersWithFilters(query.toLowerCase(), currentUser.getUserId(), pageable);
    }


    public int getHostedEventsCount(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getHostedEventsCount(); // Use the helper method from UserEntity
    }


}
