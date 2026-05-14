package com.parcezza.backend.service;

import com.parcezza.backend.dto.auth.AuthResponse;
import com.parcezza.backend.dto.auth.LoginRequest;
import com.parcezza.backend.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
