package com.softclouds.SpringAdvancedConcepts.service;

import com.softclouds.SpringAdvancedConcepts.dto.BookingDTO;
import com.softclouds.SpringAdvancedConcepts.entity.User;
import com.softclouds.SpringAdvancedConcepts.enums.Status;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BookingService {

    BookingDTO bookPackage(Long packageId, User user);

    List<BookingDTO> readBookings();

    BookingDTO readBookingById(Long bookingId);

    BookingDTO cancelBookingById(Long bookingId);

    BookingDTO updateBookingStatus(Long bookingId, Status status);

    //For Current User

    List<BookingDTO> readMyBookings(User user);

    BookingDTO readMyBookingById(User user, Long bookingId) throws AccessDeniedException;

    BookingDTO cancelMyBookingById(User user, Long bookingId) throws AccessDeniedException;

}
