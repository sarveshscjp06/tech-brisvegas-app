package com.brisvegastech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollRequest {
    private String username;
    private String password;
    private Set<String> roles;
    private String email;
    private String mobile;
    private Set<String> status;
}