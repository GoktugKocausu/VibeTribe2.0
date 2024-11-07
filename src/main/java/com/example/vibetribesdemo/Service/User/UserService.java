package com.example.vibetribesdemo.Service.User;

import com.example.vibetribesdemo.DTOs.User.UserProfileUpdateDto;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserEntity updateUserProfile(String username, UserProfileUpdateDto userProfileUpdateDto);
    String uploadProfilePicture(String username, MultipartFile file);
}