package com.klm.test.klm_test.controller;

import com.klm.test.klm_test.dto.BookingValueDto;
import com.klm.test.klm_test.dto.CreateBookingDto;
import com.klm.test.klm_test.service.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingValueDto addBooking(@RequestBody @Valid CreateBookingDto createBookingDto) {
        log.info("AddBooking: {}", createBookingDto);
        return bookingService.createBooking(createBookingDto);
    }

    @GetMapping("/by-departure-time")
    public List<BookingValueDto> getBookingsByDepartureTime(@RequestParam ZonedDateTime departureTime){
        log.info("getBookingsByDepartureTime: {}", departureTime);
        return bookingService.findBookingsBefore(departureTime);
    }

    @GetMapping("/by-visited-airports")
        public List<BookingValueDto> getBookingsByVisitedAirportsSequentially(@RequestParam String departureAirport, @RequestParam String arrivalAirport){
        log.info("getBookingsByVisitedAirportsSequentially: {}", departureAirport);
        return bookingService.findBookingsByVisitedAirportsSequentially(departureAirport, arrivalAirport);
    }
}
