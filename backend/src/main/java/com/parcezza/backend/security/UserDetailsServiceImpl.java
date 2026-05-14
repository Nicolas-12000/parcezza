package com.parcezza.backend.security;

import com.parcezza.backend.domain.User;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserPrincipal(user);
    }
}
