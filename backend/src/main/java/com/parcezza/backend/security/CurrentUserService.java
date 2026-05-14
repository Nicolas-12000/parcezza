package com.parcezza.backend.security;

import com.parcezza.backend.domain.User;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.exception.UnauthorizedException;
import com.parcezza.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new UnauthorizedException("User not authenticated");
        }

        return userRepository.findByEmail(principal.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
