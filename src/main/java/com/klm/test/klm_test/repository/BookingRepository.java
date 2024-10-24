package com.klm.test.klm_test.repository;

import com.klm.test.klm_test.dto.BookingValueDto;
import com.klm.test.klm_test.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select booking from Booking booking where booking.id = :pId")
    BookingValueDto findValueById(@Param("pId") Long id);

    List<BookingValueDto> findAllByDepartureBeforeOrderById(Instant departureTime);

    @Query("select booking from Booking booking " +
            "join booking.flightList flight " +
            "where flight.departureAirport.iataCode = :pDeparture and flight.arrivalAirport.iataCode = :pArrival " +
            "order by booking.id")
    List<BookingValueDto> findAllByDepartureAndArrival(@Param("pDeparture") String departure, @Param("pArrival") String arrival);
}
