package com.sk.syncboard.dto;

import com.sk.syncboard.model.Role;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private Role role;
}
