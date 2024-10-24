package com.klm.test.klm_test.controller;

import com.klm.test.klm_test.dto.BookingValueDto;
import com.klm.test.klm_test.dto.CreateBookingDto;
import com.klm.test.klm_test.dto.FlightsDto;
import com.klm.test.klm_test.model.BookingValueDtoTest;
import com.klm.test.klm_test.repository.BookingRepository;
import com.klm.test.klm_test.repository.FlightRepository;
import com.klm.test.klm_test.service.BookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    public void cleanDb() {
        flightRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void addBooking_withOneFlight_shouldWork() {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        ZonedDateTime departureTime = ZonedDateTime.of(LocalDate.of(2020,5,26),
                LocalTime.of(6,45), ZoneId.of("Europe/Budapest"));
        createBookingDto.setDeparture(departureTime);
        createBookingDto.setPaxName("Alice");
        FlightsDto flightsDto = new FlightsDto();
        flightsDto.setDepartureAirport("JFK");
        flightsDto.setArrivalAirport("AMS");
        createBookingDto.setFlightsDtoList(List.of(flightsDto));

        ResponseEntity<BookingValueDtoTest> result = restTemplate.postForEntity(String.format("http://localhost:%d/api/v1/bookings", port), createBookingDto, BookingValueDtoTest.class);
        Assertions.assertTrue(result.getStatusCode().is2xxSuccessful());
        BookingValueDto resultBody = result.getBody();
        Assertions.assertNotNull(resultBody);
        Assertions.assertNotNull(resultBody.getId());
        Assertions.assertEquals(departureTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant(),resultBody.getDeparture());
        Assertions.assertEquals("JFK-AMS", resultBody.getItinerary());
    }

    @Test
    public void addBooking_withInvalidAirportCode_shouldThrowException() {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        ZonedDateTime departureTime = ZonedDateTime.of(LocalDate.of(2020,5,26),
                LocalTime.of(6,45), ZoneId.of("Europe/Budapest"));
        createBookingDto.setDeparture(departureTime);
        createBookingDto.setPaxName("Alice");
        FlightsDto flightsDto = new FlightsDto();
        flightsDto.setDepartureAirport("BUD");
        flightsDto.setArrivalAirport("AMS");
        createBookingDto.setFlightsDtoList(List.of(flightsDto));

        ResponseEntity<BookingValueDtoTest> result = restTemplate.postForEntity(String.format("http://localhost:%d/api/v1/bookings", port), createBookingDto, BookingValueDtoTest.class);
        Assertions.assertTrue(result.getStatusCode().is4xxClientError());
    }

    @Test
    public void addBooking_withInvalidRequest_shouldThrowException() {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        createBookingDto.setPaxName("Alice");
        FlightsDto flightsDto = new FlightsDto();
        flightsDto.setDepartureAirport("JFK");
        flightsDto.setArrivalAirport("AMS");
        createBookingDto.setFlightsDtoList(List.of(flightsDto));

        ResponseEntity<BookingValueDtoTest> result = restTemplate.postForEntity(String.format("http://localhost:%d/api/v1/bookings", port), createBookingDto, BookingValueDtoTest.class);
        Assertions.assertTrue(result.getStatusCode().is4xxClientError());
    }

    @Test
    public void addBooking_withOneLayover_shouldWork() {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        ZonedDateTime departureTime = ZonedDateTime.of(LocalDate.of(2024,6,4),
                LocalTime.of(11,4), ZoneId.of("Europe/Budapest"));
        createBookingDto.setDeparture(departureTime);
        createBookingDto.setPaxName("Bruce");
        FlightsDto flightsDto = new FlightsDto();
        flightsDto.setDepartureAirport("GVA");
        flightsDto.setArrivalAirport("AMS");

        FlightsDto flightsDto2 = new FlightsDto();
        flightsDto2.setDepartureAirport("AMS");
        flightsDto2.setArrivalAirport("LHR");
        createBookingDto.setFlightsDtoList(List.of(flightsDto,flightsDto2));

        ResponseEntity<BookingValueDtoTest> result = restTemplate.postForEntity(String.format("http://localhost:%d/api/v1/bookings", port), createBookingDto, BookingValueDtoTest.class);
        Assertions.assertTrue(result.getStatusCode().is2xxSuccessful());
        BookingValueDto resultBody = result.getBody();
        Assertions.assertNotNull(resultBody);
        Assertions.assertNotNull(resultBody.getId());
        Assertions.assertEquals(departureTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant(),resultBody.getDeparture());
        Assertions.assertEquals("GVA-AMS-LHR", resultBody.getItinerary());
    }

    @Test
    public void addBooking_withMultipleLayovers_shouldWork() {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        ZonedDateTime departureTime = ZonedDateTime.of(LocalDate.of(2024,6,6),
                LocalTime.of(10,0), ZoneId.of("Europe/Budapest"));
        createBookingDto.setDeparture(departureTime);
        createBookingDto.setPaxName("Cindy");
        FlightsDto flightsDto = new FlightsDto();
        flightsDto.setDepartureAirport("AAL");
        flightsDto.setArrivalAirport("AMS");

        FlightsDto flightsDto2 = new FlightsDto();
        flightsDto2.setDepartureAirport("AMS");
        flightsDto2.setArrivalAirport("LHR");

        FlightsDto flightsDto3 = new FlightsDto();
        flightsDto3.setDepartureAirport("LHR");
        flightsDto3.setArrivalAirport("JFK");

        FlightsDto flightsDto4 = new FlightsDto();
        flightsDto4.setDepartureAirport("JFK");
        flightsDto4.setArrivalAirport("SFO");

        createBookingDto.setFlightsDtoList(List.of(flightsDto,flightsDto2,flightsDto3,flightsDto4));

        ResponseEntity<BookingValueDtoTest> result = restTemplate.postForEntity(String.format("http://localhost:%d/api/v1/bookings", port), createBookingDto, BookingValueDtoTest.class);
        Assertions.assertTrue(result.getStatusCode().is2xxSuccessful());
        BookingValueDto resultBody = result.getBody();
        Assertions.assertNotNull(resultBody);
        Assertions.assertNotNull(resultBody.getId());
        Assertions.assertEquals(departureTime.withZoneSameInstant(ZoneId.of("UTC")).toInstant(),resultBody.getDeparture());
        Assertions.assertEquals("AAL-AMS-LHR-JFK-SFO", resultBody.getItinerary());
    }

    @Test
    public void getBookingsByDepartureTime_withValidData_shouldReturnTwoResult() {
        createBookings();
        ZonedDateTime departureTime = ZonedDateTime.of(LocalDate.of(2024,6,4),
                LocalTime.of(11,5), ZoneId.of("Europe/Budapest"));
        ResponseEntity<BookingValueDtoTest[]> response = restTemplate.getForEntity(String.format("http://localhost:%d/api/v1/bookings/by-departure-time?departureTime={departureTime}", port), BookingValueDtoTest[].class, Map.of("departureTime", departureTime));
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        List<BookingValueDtoTest> resultBody = Arrays.stream(response.getBody()).toList();
        Assertions.assertEquals(2, resultBody.size());
        Assertions.assertEquals("Alice", resultBody.get(0).getPaxName());
        Assertions.assertEquals("Bruce", resultBody.get(1).getPaxName());
    }

    @Test
    public void getBookingsByDepartureTime_withoutData_shouldReturnEmptyList() {
        createBookings();
        ZonedDateTime departureTime = ZonedDateTime.of(LocalDate.of(2019,6,4),
                LocalTime.of(11,5), ZoneId.of("Europe/Budapest"));
        ResponseEntity<BookingValueDtoTest[]> response = restTemplate.getForEntity(String.format("http://localhost:%d/api/v1/bookings/by-departure-time?departureTime={departureTime}", port), BookingValueDtoTest[].class, Map.of("departureTime", departureTime));
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        List<BookingValueDtoTest> resultBody = Arrays.stream(response.getBody()).toList();
        Assertions.assertEquals(0, resultBody.size());
    }

    @Test
    public void getBookingsByVisitedAirportsSequentially_withValidData_shouldReturnTwoResult() {
        createBookings();
        String departure = "AMS";
        String arrival = "LHR";

        ResponseEntity<BookingValueDtoTest[]> response = restTemplate.getForEntity(String.format("http://localhost:%d/api/v1/bookings/by-visited-airports?departureAirport={departureAirport}&arrivalAirport={arrivalAirport}", port),
                BookingValueDtoTest[].class,
                Map.of("departureAirport", departure,"arrivalAirport", arrival));
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        List<BookingValueDtoTest> resultBody = Arrays.stream(response.getBody()).toList();
        Assertions.assertEquals(2, resultBody.size());
        Assertions.assertEquals("AAL-AMS-LHR-JFK-SFO", resultBody.get(0).getItinerary());
        Assertions.assertEquals("GVA-AMS-LHR", resultBody.get(1).getItinerary());
    }

    @Test
    public void getBookingsByVisitedAirportsSequentially_withInvalidRequestData_shouldReturnEmptyList() {
        createBookings();
        String departure = "AMS";
        String arrival = "GVA";

        ResponseEntity<BookingValueDtoTest[]> response = restTemplate.getForEntity(String.format("http://localhost:%d/api/v1/bookings/by-visited-airports?departureAirport={departureAirport}&arrivalAirport={arrivalAirport}", port),
                BookingValueDtoTest[].class,
                Map.of("departureAirport", departure,"arrivalAirport", arrival));
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        List<BookingValueDtoTest> resultBody = Arrays.stream(response.getBody()).toList();
        Assertions.assertEquals(0, resultBody.size());
    }

    private void createBookings() {
        CreateBookingDto createBookingDto = new CreateBookingDto();
        ZonedDateTime departureTime = ZonedDateTime.of(LocalDate.of(2024,6,6),
                LocalTime.of(10,0), ZoneId.of("Europe/Budapest"));
        createBookingDto.setDeparture(departureTime);
        createBookingDto.setPaxName("Cindy");
        FlightsDto flightsDto = new FlightsDto();
        flightsDto.setDepartureAirport("AAL");
        flightsDto.setArrivalAirport("AMS");

        FlightsDto flightsDto2 = new FlightsDto();
        flightsDto2.setDepartureAirport("AMS");
        flightsDto2.setArrivalAirport("LHR");

        FlightsDto flightsDto3 = new FlightsDto();
        flightsDto3.setDepartureAirport("LHR");
        flightsDto3.setArrivalAirport("JFK");

        FlightsDto flightsDto4 = new FlightsDto();
        flightsDto4.setDepartureAirport("JFK");
        flightsDto4.setArrivalAirport("SFO");

        createBookingDto.setFlightsDtoList(List.of(flightsDto,flightsDto2,flightsDto3,flightsDto4));

        bookingService.createBooking(createBookingDto);

        createBookingDto = new CreateBookingDto();
        departureTime = ZonedDateTime.of(LocalDate.of(2020,5,26),
                LocalTime.of(6,45), ZoneId.of("Europe/Budapest"));
        createBookingDto.setDeparture(departureTime);
        createBookingDto.setPaxName("Alice");
        flightsDto = new FlightsDto();
        flightsDto.setDepartureAirport("JFK");
        flightsDto.setArrivalAirport("AMS");
        createBookingDto.setFlightsDtoList(List.of(flightsDto));

        bookingService.createBooking(createBookingDto);

        createBookingDto = new CreateBookingDto();
        departureTime = ZonedDateTime.of(LocalDate.of(2024,6,4),
                LocalTime.of(11,4), ZoneId.of("Europe/Budapest"));
        createBookingDto.setDeparture(departureTime);
        createBookingDto.setPaxName("Bruce");
        flightsDto = new FlightsDto();
        flightsDto.setDepartureAirport("GVA");
        flightsDto.setArrivalAirport("AMS");

        flightsDto2 = new FlightsDto();
        flightsDto2.setDepartureAirport("AMS");
        flightsDto2.setArrivalAirport("LHR");
        createBookingDto.setFlightsDtoList(List.of(flightsDto,flightsDto2));

        bookingService.createBooking(createBookingDto);
    }
}
