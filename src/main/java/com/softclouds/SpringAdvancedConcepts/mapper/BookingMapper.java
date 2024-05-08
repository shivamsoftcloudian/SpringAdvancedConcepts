package com.softclouds.SpringAdvancedConcepts.mapper;

import com.softclouds.SpringAdvancedConcepts.dto.BookingDTO;
import com.softclouds.SpringAdvancedConcepts.entity.Booking;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toEntity(BookingDTO bookingDTO);

    BookingDTO toDTO(Booking booking);

    List<Booking> toEntity(List<BookingDTO> bookingDTOS);

    List<BookingDTO> toDTO(List<Booking> bookingList);
}
