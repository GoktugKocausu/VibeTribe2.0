package com.example.vibetribesdemo.Service;

import com.example.vibetribesdemo.DTOs.User.LoginRequestDto;
import com.example.vibetribesdemo.DTOs.User.RegisterRequestDto;
import com.example.vibetribesdemo.entities.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {
    ResponseEntity<?> registerUser(RegisterRequestDto registerRequestDto );
    ResponseEntity<?> authenticateUser(LoginRequestDto loginRequestDto);
}
