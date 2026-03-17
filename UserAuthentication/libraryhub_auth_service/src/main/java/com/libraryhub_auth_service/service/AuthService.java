package com.libraryhub_auth_service.service;

import com.libraryhub_auth_service.dto.LoginRequest;
import com.libraryhub_auth_service.dto.RegisterRequest;
import com.libraryhub_auth_service.entity.User;
import com.libraryhub_auth_service.repository
        .UserRepository;
import com.libraryhub_auth_service.security.JwtUtil;
import org.springframework.security.crypto.password
        .PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    
    public String register(RegisterRequest request) {

        
        if (userRepository.existsByEmail(
                request.getEmail())) {
            throw new RuntimeException(
                "Email already registered");
        }

      
        User.Role role = request.getRole();
        if (role == null) {
            role = User.Role.STUDENT;
        }

       
        User user = User.builder()
            .username(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(
                request.getPassword()))
            .role(role)
            .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    
    public Map<String, Object> login(
            LoginRequest request) {

        
        User user = userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException(
                "Invalid email or password"));

        
        boolean passwordMatch = passwordEncoder.matches(
            request.getPassword(),
            user.getPassword());

        if (!passwordMatch) {
            throw new RuntimeException(
                "Invalid email or password");
        }

    
        String token = jwtUtil.generateToken(
            user.getEmail(),
            user.getRole().name(),
            user.getId()
        );

        
        Map<String, Object> response = new HashMap<>();
        response.put("token",   token);
        response.put("name",    user.getUsername());
        response.put("email",   user.getEmail());
        response.put("role",    user.getRole().name());
        response.put("userId",  user.getId());

        return response;
    }
}