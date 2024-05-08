package com.softclouds.SpringAdvancedConcepts.controller;

import com.softclouds.SpringAdvancedConcepts.dto.BookingDTO;
import com.softclouds.SpringAdvancedConcepts.entity.User;
import com.softclouds.SpringAdvancedConcepts.enums.Role;
import com.softclouds.SpringAdvancedConcepts.enums.Status;
import com.softclouds.SpringAdvancedConcepts.exception.ElementNotExistException;
import com.softclouds.SpringAdvancedConcepts.repository.UserRepository;
import com.softclouds.SpringAdvancedConcepts.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/samplespring/v1")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final UserRepository userRepository;

    @PostMapping("/bookings/package/{packageId}")
    public ResponseEntity<BookingDTO> bookPackage(@PathVariable Long packageId) {
        return new ResponseEntity<>(this.bookingService.bookPackage(packageId, getCurrentUser()), HttpStatus.OK);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {

        User user = getCurrentUser();
        if (user.getRole().equals(Role.ADMIN)) {
            return new ResponseEntity<>(this.bookingService.readBookings(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(this.bookingService.readMyBookings(user), HttpStatus.OK);
        }
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId) throws AccessDeniedException {
        User user = getCurrentUser();
        if (user.getRole().equals(Role.ADMIN)) {
            return new ResponseEntity<>(this.bookingService.readBookingById(bookingId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(this.bookingService.readMyBookingById(user, bookingId), HttpStatus.OK);
        }
    }

    @PutMapping("/bookings/cancel/{bookingId}")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable Long bookingId) throws AccessDeniedException {

        User user = getCurrentUser();
        if (user.getRole().equals(Role.ADMIN)) {
            return new ResponseEntity<>(this.bookingService.cancelBookingById(bookingId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(this.bookingService.cancelMyBookingById(user, bookingId), HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/bookings/{bookingId}")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long bookingId, @RequestParam Status status) {
        return new ResponseEntity<>(this.bookingService.updateBookingStatus(bookingId, status), HttpStatus.OK);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetail.getUsername());
        if (user != null) {
            return
                    userRepository.findByUsername(userDetail.getUsername());
        } else
            throw new ElementNotExistException("User not Found");

    }

}
