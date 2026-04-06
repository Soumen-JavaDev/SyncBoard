package com.sk.syncboard.controller;

import com.sk.syncboard.dto.CreateUserRequest;
import com.sk.syncboard.dto.UserResponse;
import com.sk.syncboard.model.User;
import com.sk.syncboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/team")
    public ResponseEntity<List<UserResponse>> getTeamOfOrg(){
       return ResponseEntity.ok(userService.getTeam());
    }
}