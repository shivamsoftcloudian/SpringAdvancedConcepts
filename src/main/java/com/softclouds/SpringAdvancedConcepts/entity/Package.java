package com.softclouds.SpringAdvancedConcepts.entity;

import com.softclouds.SpringAdvancedConcepts.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String packageName;
    private String packageDescription;
    private int price;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany
    private List<Booking> bookings = new ArrayList<>();
}
