package com.klm.test.klm_test.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_id_seq_gen")
    @SequenceGenerator(name = "flight_id_seq_gen", sequenceName = "flight_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
