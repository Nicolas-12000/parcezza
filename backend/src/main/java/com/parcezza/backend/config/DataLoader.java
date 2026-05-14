package com.parcezza.backend.config;

import com.parcezza.backend.domain.Role;
import com.parcezza.backend.domain.User;
import com.parcezza.backend.repository.RoleRepository;
import com.parcezza.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@Profile("dev")
public class DataLoader {

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            Optional<Role> rUser = roleRepository.findByRoleName("ROLE_USER");
            if (rUser.isEmpty()) roleRepository.save(new Role("ROLE_USER"));
            Optional<Role> rAdmin = roleRepository.findByRoleName("ROLE_ADMIN");
            if (rAdmin.isEmpty()) roleRepository.save(new Role("ROLE_ADMIN"));
            Optional<Role> rSeller = roleRepository.findByRoleName("ROLE_SELLER");
            if (rSeller.isEmpty()) roleRepository.save(new Role("ROLE_SELLER"));

            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setFullName("System Admin");
                admin.setPasswordHash(encoder.encode("admin"));
                admin.setEnabled(true);
                Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN").get();
                admin.getRoles().add(adminRole);
                userRepository.save(admin);
            }
        };
    }
}
