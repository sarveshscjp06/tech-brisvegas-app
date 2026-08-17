package com.brisvegastech.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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