package com.klm.test.klm_test.service.impl;

import com.klm.test.klm_test.dto.BookingValueDto;
import com.klm.test.klm_test.dto.CreateBookingDto;
import com.klm.test.klm_test.dto.FlightsDto;
import com.klm.test.klm_test.model.Booking;
import com.klm.test.klm_test.model.Flight;
import com.klm.test.klm_test.repository.BookingRepository;
import com.klm.test.klm_test.service.AirportService;
import com.klm.test.klm_test.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;

    private AirportService airportService;


    @Transactional
    @Override
    public BookingValueDto createBooking(CreateBookingDto createBookingDto) {
        Booking booking = new Booking();
        booking.setDeparture(createBookingDto.getDeparture().toInstant());
        booking.setPaxName(createBookingDto.getPaxName());
        booking.addFlightList(createBookingDto.getFlightsDtoList()
                .stream()
                .map(this::mapFlightDtoToEntity)
                .toList());
        booking = bookingRepository.save(booking);
        return bookingRepository.findValueById(booking.getId());
    }

    private Flight mapFlightDtoToEntity(FlightsDto flightDto) {
        Flight flight = new Flight();
        flight.setDepartureAirport(airportService.getAirportByCode(flightDto.getDepartureAirport()));
        flight.setArrivalAirport(airportService.getAirportByCode(flightDto.getArrivalAirport()));
        return flight;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingValueDto> findBookingsBefore(ZonedDateTime departureTime) {
        return bookingRepository.findAllByDepartureBeforeOrderById(departureTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingValueDto> findBookingsByVisitedAirportsSequentially(String departureAirport, String arrivalAirport) {
        return bookingRepository.findAllByDepartureAndArrival(departureAirport,arrivalAirport);
    }
}
