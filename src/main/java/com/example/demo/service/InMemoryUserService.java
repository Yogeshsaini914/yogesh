package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public InMemoryUserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostConstruct
    void seedUsers() {
        userRepository.findById("user").orElseGet(() ->
                userRepository.save(new AppUser("user", passwordEncoder.encode("user123"), Role.ROLE_USER)));
        userRepository.findById("admin").orElseGet(() ->
                userRepository.save(new AppUser("admin", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole().name().replace("ROLE_", ""))
                .build();
    }

    public boolean registerUser(String username, String rawPassword) {
        if (userRepository.existsById(username)) {
            return false;
        }

        userRepository.save(new AppUser(username, passwordEncoder.encode(rawPassword), Role.ROLE_USER));
        return true;
    }
}
