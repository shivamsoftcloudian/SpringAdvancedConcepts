package com.softclouds.SpringAdvancedConcepts.service.serviceImpl;

import com.softclouds.SpringAdvancedConcepts.dto.BookingDTO;
import com.softclouds.SpringAdvancedConcepts.entity.Booking;
import com.softclouds.SpringAdvancedConcepts.entity.Package;
import com.softclouds.SpringAdvancedConcepts.entity.User;
import com.softclouds.SpringAdvancedConcepts.enums.Status;
import com.softclouds.SpringAdvancedConcepts.exception.ElementAlreadyExistException;
import com.softclouds.SpringAdvancedConcepts.exception.ElementNotExistException;
import com.softclouds.SpringAdvancedConcepts.mapper.BookingMapper;
import com.softclouds.SpringAdvancedConcepts.repository.BookingRepository;
import com.softclouds.SpringAdvancedConcepts.repository.PackageRepository;
import com.softclouds.SpringAdvancedConcepts.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PackageRepository packageRepository;

    private final BookingMapper bookingMapper;

    @Override
    public BookingDTO bookPackage(Long packageId, User user) {

        Package travelPackage = packageRepository.findById(packageId).orElseThrow(
                () -> new ElementNotExistException("Package does not exist with package-id: " + packageId));
        Booking booking = new Booking();
        booking.setTravelPackage(travelPackage);
        booking.setBookingDateTime(new Date(System.currentTimeMillis()));
        booking.setBookingNumber("123ad4890asd65sa3");
        booking.setBookingDestination("Delhi");
        booking.setBookingStatus(Status.PENDING);
        booking.getUsers().add(user);

        booking = bookingRepository.save(booking);
        booking = bookingRepository.save(booking);


        return bookingMapper.toDTO(booking);
    }

    @Override
    public List<BookingDTO> readBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        if (!bookings.isEmpty()) {
            return bookingMapper.toDTO(bookings);
        }
        throw new ElementNotExistException("No Bookings Found !!!");
    }

    @Override
    public BookingDTO readBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ElementNotExistException("Booking does not exist with booking-id: " + bookingId));
        return bookingMapper.toDTO(booking);
    }

    @Override
    public BookingDTO cancelBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ElementNotExistException("Booking does not exist with booking-id: " + bookingId));
        booking.setUpdateBookingDateTime(new Date(System.currentTimeMillis()));
        booking.setBookingStatus(Status.CANCELED);
        booking = bookingRepository.save(booking);
        log.info("Booking cancelled: {}", booking.getBookingNumber());
        return bookingMapper.toDTO(booking);
    }

    @Override
    public BookingDTO updateBookingStatus(Long bookingId, Status status) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ElementNotExistException("Booking does not exist with booking-id: " + bookingId));

        if (booking.getBookingStatus() == status) {
            throw new ElementAlreadyExistException("Booking status is already " + status);
        }

        booking.setUpdateBookingDateTime(new Date(System.currentTimeMillis()));
        booking.setBookingStatus(status);
        booking = bookingRepository.save(booking);

        log.info("Booking status is changed to: {}", status);

        return bookingMapper.toDTO(booking);
    }

    // For Current User :-

    @Override
    public List<BookingDTO> readMyBookings(User user) {
        List<Booking> bookings = bookingRepository.findAll();
        List<Booking> myBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUsers().contains(user)) {
                myBookings.add(booking);
            }
        }
        return bookingMapper.toDTO(myBookings);
    }

    @Override
    public BookingDTO readMyBookingById(User user, Long bookingId) throws AccessDeniedException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ElementNotExistException("Booking does not exist with booking-id: " + bookingId));
        if (user.getBookings().contains(booking)) {
            return bookingMapper.toDTO(booking);
        } else
            throw new AccessDeniedException("Access denied");
    }

    @Override
    public BookingDTO cancelMyBookingById(User user, Long bookingId) throws AccessDeniedException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ElementNotExistException("Booking does not exist with booking-id: " + bookingId));
        if (booking.getBookingStatus() == Status.CANCELED) {
            throw new ElementAlreadyExistException("Booking status is already " + Status.CANCELED);
        }
        if (user.getBookings().contains(booking)) {
            booking.setUpdateBookingDateTime(new Date(System.currentTimeMillis()));
            booking.setBookingStatus(Status.CANCELED);
            booking = bookingRepository.save(booking);
            log.info("Booking cancelled by user: {}", booking.getBookingNumber());
            return bookingMapper.toDTO(booking);
        } else
            throw new AccessDeniedException("Access denied");
    }

}
