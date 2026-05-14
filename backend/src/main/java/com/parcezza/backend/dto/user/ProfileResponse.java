package com.parcezza.backend.dto.user;

import java.util.Set;

public record ProfileResponse(
    Long id,
    String email,
    String fullName,
    boolean enabled,
    Set<String> roles
) {
}
