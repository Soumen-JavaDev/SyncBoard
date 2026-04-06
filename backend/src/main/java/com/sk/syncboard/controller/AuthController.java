package com.sk.syncboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sk.syncboard.dto.AuthResponse;
import com.sk.syncboard.dto.LoginRequest;
import com.sk.syncboard.dto.RegisterRequest;
import com.sk.syncboard.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
 
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){

      return  authService.register(registerRequest);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
      return authService.login(loginRequest);
        
    }
}
