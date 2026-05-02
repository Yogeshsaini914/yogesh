package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.model.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryUserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final Map<String, AppUser> users = new ConcurrentHashMap<>();

    public InMemoryUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    void seedUsers() {
        users.put("user", new AppUser("user", passwordEncoder.encode("user123"), Role.ROLE_USER));
        users.put("admin", new AppUser("admin", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = users.get(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username(appUser.username())
                .password(appUser.password())
                .roles(appUser.role().name().replace("ROLE_", ""))
                .build();
    }

    public boolean registerUser(String username, String rawPassword) {
        AppUser newUser = new AppUser(username, passwordEncoder.encode(rawPassword), Role.ROLE_USER);
        return users.putIfAbsent(username, newUser) == null;
    }
}
