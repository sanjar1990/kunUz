package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    //login
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO>login(@Valid @RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(authService.login(authDTO));
    }
    //register user by email
    @PostMapping("/registrationByEmail")
    public ResponseEntity<ApiResponseDTO>registrationByEmail(@Valid @RequestBody RegistrationDTO registrationDTO){
        return ResponseEntity.ok(authService.registrationByEmail(registrationDTO));
    }
    //register user by phone
    @PostMapping("/registrationByPhone")
    public ResponseEntity<ApiResponseDTO>registrationByPhone(@Valid @RequestBody RegistrationDTO registrationDTO){
        return ResponseEntity.ok(authService.registrationByPhone(registrationDTO));
    }
    //email verification
    @GetMapping("/verification/email{jwt}")
    public ResponseEntity<ApiResponseDTO>emailVerification(@PathVariable String jwt){
        return ResponseEntity.ok(authService.emailVerification(jwt));
    }
    //phone verification
    @GetMapping("/verification/phone")
    public ResponseEntity<?>phoneVerification(@RequestParam("phone") String phone,
                                              @RequestParam("message") String message){
        return ResponseEntity.ok(authService.phoneVerification("+"+phone,message));
    }
}
