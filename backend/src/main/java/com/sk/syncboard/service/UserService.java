package com.sk.syncboard.service;

import com.sk.syncboard.dto.CreateUserRequest;
import com.sk.syncboard.dto.UserResponse;
import com.sk.syncboard.model.Role;
import com.sk.syncboard.model.User;
import com.sk.syncboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<?> createUser(CreateUserRequest request) {

        User currentUser = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // ONLY ADMIN CAN CREATE
        if (currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admin can create users");
        }

        User newUser = User.builder()
                .first_name(request.getFirstName())
                .last_name(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(new BCryptPasswordEncoder().encode(request.getPassword()))
                .role(request.getRole())
                .organization(currentUser.getOrganization()) // SAME ORG
                .createdBy(currentUser)
                .build();

        userRepository.save(newUser);

        return ResponseEntity.ok("User created successfully");
    }

    public  List<UserResponse> getTeam() {
        User currentUser = (User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        return userRepository.findByOrganizationId(
                currentUser.getOrganization().getId()
        ).stream().map(user -> UserResponse.builder()
                .id(user.getId())
                .name(user.getFirst_name() + " " + user.getLast_name())
                .email(user.getEmail())
                .role(user.getRole().name())
                .phone(user.getPhone())
                .active(user.isEnabled())
                .build()
        ).toList();
    }
}