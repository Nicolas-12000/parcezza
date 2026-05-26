package com.parcezza.backend;

import com.parcezza.backend.dto.auth.LoginRequest;
import com.parcezza.backend.dto.auth.AuthResponse;
import com.parcezza.backend.dto.auth.RegisterRequest;
import com.parcezza.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthControllerTest {

    @Autowired
    private AuthService authService;

    @Test
    void registerReturnsToken() throws Exception {
        RegisterRequest request = new RegisterRequest("newuser@example.com", "New User", "password123");
        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }

    @Test
    void loginReturnsToken() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("login@example.com", "Login User", "password123");
        authService.register(registerRequest);

        LoginRequest request = new LoginRequest("login@example.com", "password123", false);
        AuthResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.tokenType()).isEqualTo("Bearer");
    }
}
