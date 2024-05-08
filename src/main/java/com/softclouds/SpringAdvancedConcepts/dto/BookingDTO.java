package com.softclouds.SpringAdvancedConcepts.dto;

import com.softclouds.SpringAdvancedConcepts.enums.Status;
import lombok.Data;

import java.sql.Date;

@Data
public class BookingDTO {

    private Long id;
    private Date bookingDateTime;
    private String bookingDestination;
    private String bookingNumber;
    private Status bookingStatus;

}
