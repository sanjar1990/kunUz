package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    //login
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO>login(@RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(authService.login(authDTO));
    }
    //register user
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO>register(@RequestBody ProfileDTO profileDTO){
        return ResponseEntity.ok(authService.registration(profileDTO));
    }
}
