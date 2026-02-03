package com.example.service;

import com.example.dto.JwtResponse;
import com.example.dto.SigninRequest;
import com.example.dto.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    JwtResponse authenticateUser(SigninRequest loginRequest);

    ResponseEntity<?> registerUser(SignupRequest signUpRequest);
}
