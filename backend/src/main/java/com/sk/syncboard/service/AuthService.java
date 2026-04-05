package com.sk.syncboard.service;

import com.sk.syncboard.dto.*;
import com.sk.syncboard.model.*;
import com.sk.syncboard.repository.OrganizationRepository;
import com.sk.syncboard.repository.UserRepository;
import com.sk.syncboard.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Transactional // Ensures both Org and User are saved or neither is
    public ResponseEntity<?> register(RegisterRequest registerRequest) {

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // 1. Find or create organization
        Organization org = organizationRepository
                .findByName(registerRequest.getOrganizationName())
                .orElseGet(() -> {
                    Organization newOrg = Organization.builder()
                            .name(registerRequest.getOrganizationName())
                            .build();
                    return organizationRepository.save(newOrg);
                });

        // 2. Create user
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ADMIN)
                .organization(org)
                .build();

        userRepository.save(user);

        // 3. Generate token so the user is logged in immediately after signing up
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}