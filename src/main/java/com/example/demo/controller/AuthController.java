package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.security.JwtService;
import com.example.demo.service.InMemoryUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final InMemoryUserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
                          InMemoryUserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails principal = (UserDetails) auth.getPrincipal();
        String jwtToken = jwtService.generateToken(principal);
        return new AuthResponse(jwtToken);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        boolean created = userService.registerUser(request.username(), request.password());
        if (!created) {
            throw new ResponseStatusException(BAD_REQUEST, "Username already exists");
        }

        UserDetails principal = userService.loadUserByUsername(request.username());
        String jwtToken = jwtService.generateToken(principal);
        return new AuthResponse(jwtToken);
    }
}
