package com.softclouds.SpringAdvancedConcepts.entity;

import com.softclouds.SpringAdvancedConcepts.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isLocked;

    private Date lockExpiration;


    @ManyToMany(mappedBy = "users")
    private Set<Booking> bookings = new HashSet<>();
}
