package com.softclouds.SpringAdvancedConcepts.security;

import com.softclouds.SpringAdvancedConcepts.security.payload.JwtResponseDTO;
import com.softclouds.SpringAdvancedConcepts.security.payload.LoginRequest;
import com.softclouds.SpringAdvancedConcepts.security.payload.RegisterRequest;

import javax.security.auth.login.AccountLockedException;

public interface AuthService {

    JwtResponseDTO authenticate(LoginRequest request) throws AccountLockedException;

    String register(RegisterRequest request);
}
