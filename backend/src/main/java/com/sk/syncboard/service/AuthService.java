package com.sk.syncboard.service;

import com.sk.syncboard.repository.OrganizationRepository;
import com.sk.syncboard.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sk.syncboard.dto.*;
import com.sk.syncboard.model.*;
import com.sk.syncboard.repository.UserRepository;
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public ResponseEntity<?> register(RegisterRequest registerRequest) {

        // Check if email already exists or not
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Find or create organization
        Organization org = organizationRepository
                .findByName(registerRequest.getOrganizationName())
                .orElseGet(() -> {
                    Organization newOrg = Organization.builder()
                            .name(registerRequest.getOrganizationName())
                            .build();
                    return organizationRepository.save(newOrg);
                });

        // Create user
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ADMIN)
                .organization(org)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("User and organization created successfully");
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // TODO: Generated Token for JWT

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}