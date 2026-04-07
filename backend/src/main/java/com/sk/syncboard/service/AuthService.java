package com.sk.syncboard.service;

import com.sk.syncboard.dto.*;
import com.sk.syncboard.model.*;
import com.sk.syncboard.repository.OrganizationRepository;
import com.sk.syncboard.repository.UserRepository;
import com.sk.syncboard.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;

    @Transactional // Ensures both Org and User are saved
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
                            .email(registerRequest.getOrgEmail())
                            .description(registerRequest.getOrgDescription())
                            .phone(registerRequest.getOrgPhone())
                            .address(registerRequest.getOrgPhone())
                            .website(registerRequest.getOrgWebsite())
                            .build();
                    return organizationRepository.save(newOrg);
                });

        // 2. Create user
        User user = User.builder()
                .first_name(registerRequest.getFirstName())
                .last_name(registerRequest.getLastName())
                .phone(registerRequest.getPhone())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ADMIN)
                .organization(org)
                .build();

       User currUser= userRepository.save(user);

        // 3. Generate token for instant login
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token,currUser.getRole().toString(),currUser.getEmail(),currUser.getFullName()));
    }

    public ResponseEntity<AuthResponse> login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        user.getRole().name(),
                        user.getEmail(),
                        user.getFullName()
                )
        );
    }
}