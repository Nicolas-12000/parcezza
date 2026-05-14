package com.parcezza.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 4, max = 200) 
    @Pattern(regexp = "^[a-zA-Z\\s\\-.,']+$", message = "Name contains invalid characters")
    String fullName,
    @NotBlank @Size(min = 8, max = 200) 
    @Pattern(regexp = "^[\\w\\s!@#$%^&*()-+=.,]+$", message = "Invalid characters in password")
    String password
) {
}
