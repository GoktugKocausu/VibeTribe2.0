package com.example.vibetribesdemo.ServiceImplementation;

import com.example.vibetribesdemo.DTOs.RegisterRequestDto;
import com.example.vibetribesdemo.DTOs.LoginRequestDto;
import com.example.vibetribesdemo.Repository.UserRepository;
import com.example.vibetribesdemo.Service.AuthService;
import com.example.vibetribesdemo.Service.BadgeService;
import com.example.vibetribesdemo.Utilities.Role;
import com.example.vibetribesdemo.entities.UserEntity;
import com.example.vibetribesdemo.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImplementation implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BadgeService badgeService; // Inject the BadgeService

    @Autowired
    public AuthServiceImplementation(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            BadgeService badgeService // Include BadgeService in the constructor
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.badgeService = badgeService;
    }

    @Override
    public ResponseEntity<?> registerUser(RegisterRequestDto registerRequestDto) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerRequestDto.getUsername());
        newUser.setEmail(registerRequestDto.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.getPassword()));
        newUser.setPhoneNumber(registerRequestDto.getPhoneNumber());
        newUser.setSex(registerRequestDto.getSex());
        newUser.setStatus(registerRequestDto.getStatus()); // Use status from DTO

        newUser.setRole(Role.USER_ROLE);

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequestDto loginRequestDto) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        // Retrieve the user from the database
        UserEntity user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Award the "Welcome Starter" badge if not already awarded
        badgeService.awardFirstLoginBadge(user);

        // Generate a JWT token for the user
        String token = jwtService.generateToken(user);

        // Prepare the response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }
}
