package com.klm.test.klm_test.service;

import com.klm.test.klm_test.dto.BookingValueDto;
import com.klm.test.klm_test.dto.CreateBookingDto;

import java.time.ZonedDateTime;
import java.util.List;

public interface BookingService {

    BookingValueDto createBooking(CreateBookingDto createBookingDto);

    List<BookingValueDto> findBookingsBefore(ZonedDateTime departureTime);

    List<BookingValueDto> findBookingsByVisitedAirportsSequentially(String departureAirport, String arrivalAirport);

}
