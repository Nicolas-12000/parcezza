package com.parcezza.backend.service;

import com.parcezza.backend.domain.Role;
import com.parcezza.backend.domain.User;
import com.parcezza.backend.dto.auth.AuthResponse;
import com.parcezza.backend.dto.auth.LoginRequest;
import com.parcezza.backend.dto.auth.RegisterRequest;
import com.parcezza.backend.exception.DuplicateResourceException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.RoleRepository;
import com.parcezza.backend.repository.UserRepository;
import com.parcezza.backend.security.JwtService;
import com.parcezza.backend.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private static final long REMEMBER_ME_EXPIRATION_MS = 7L * 24 * 60 * 60 * 1000;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered");
        }

        Role role = roleRepository.findByRoleName(DEFAULT_ROLE)
            .orElseGet(() -> roleRepository.save(new Role(DEFAULT_ROLE)));

        User user = new User();
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        user.addRole(role);

        userRepository.save(user);

        String token = jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = request.rememberMe()
            ? jwtService.generateToken(new UserPrincipal(user), REMEMBER_ME_EXPIRATION_MS)
            : jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponse(token);
    }
}
