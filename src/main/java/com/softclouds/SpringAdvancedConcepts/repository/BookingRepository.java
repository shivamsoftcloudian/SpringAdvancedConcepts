package com.softclouds.SpringAdvancedConcepts.repository;

import com.softclouds.SpringAdvancedConcepts.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

}
