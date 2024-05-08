package com.softclouds.SpringAdvancedConcepts.entity;

import com.softclouds.SpringAdvancedConcepts.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date bookingDateTime;
    private Date updateBookingDateTime;
    private String bookingDestination;
    private String bookingNumber;

    @Enumerated(EnumType.STRING)
    private Status bookingStatus;

    @ManyToOne
    @JoinColumn(name = "fk_package")
    private Package travelPackage;

    @ManyToMany
    @JoinTable(name = "booking_user",
            joinColumns = {@JoinColumn(name = "fk_booking")},
            inverseJoinColumns = {@JoinColumn(name = "fk_user")})
    private Set<User> users = new HashSet<>();
}
