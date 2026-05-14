package com.parcezza.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 4, max = 200) String fullName,
    @NotBlank @Size(min = 8, max = 200) String password
) {
}
