package com.libraryhub_auth_service.controller;

import com.libraryhub_auth_service.dto.LoginRequest;
import com.libraryhub_auth_service.dto.RegisterRequest;
import com.libraryhub_auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

   
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        String result = authService.register(request);
        return ResponseEntity.ok(result);
    }

  
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest request) {

        Map<String, Object> result =
            authService.login(request);
        return ResponseEntity.ok(result);
    }

    
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(
            "Logged out successfully");
    }
}