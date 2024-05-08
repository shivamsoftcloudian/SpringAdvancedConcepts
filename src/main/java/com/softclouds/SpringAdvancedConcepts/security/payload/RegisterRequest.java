package com.softclouds.SpringAdvancedConcepts.security.payload;

import com.softclouds.SpringAdvancedConcepts.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;
    private Role role;

}
