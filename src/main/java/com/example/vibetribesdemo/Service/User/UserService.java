package com.example.vibetribesdemo.Service.User;

import com.example.vibetribesdemo.DTOs.UserProfileUpdateDto;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    UserEntity updateUserProfile(String username, UserProfileUpdateDto userProfileUpdateDto);
    String uploadProfilePicture(String username, MultipartFile file);
    Optional<UserEntity> findByUsername(String username);
}