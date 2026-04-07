package com.sk.syncboard.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    // org details
    private String organizationName;

    private String orgDescription;

    private String orgEmail;
    private String orgPhone;

    private String orgAddress;

    private String orgWebsite;

    //employee as admin details

    private String firstName;
    private String lastName;
    private String phone;
  private String email;
  private String password;



}