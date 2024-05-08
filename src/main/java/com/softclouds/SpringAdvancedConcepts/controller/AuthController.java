package com.softclouds.SpringAdvancedConcepts.controller;

import com.softclouds.SpringAdvancedConcepts.security.AuthService;
import com.softclouds.SpringAdvancedConcepts.security.payload.JwtResponseDTO;
import com.softclouds.SpringAdvancedConcepts.security.payload.LoginRequest;
import com.softclouds.SpringAdvancedConcepts.security.payload.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountLockedException;

@RestController
@RequestMapping("/samplespring/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> authenticateAndGetToken(@RequestBody LoginRequest request) throws AccountLockedException {
        return ResponseEntity.ok().body(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok().body(authService.register(request));
    }

}
