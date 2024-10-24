package com.klm.test.klm_test.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_seq_gen")
    @SequenceGenerator(name = "booking_id_seq_gen", sequenceName = "booking_id_seq", allocationSize = 1)
    private Long id;

    @NotEmpty
    private String paxName;

    @NotNull
    private Instant departure;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flight> flightList = new ArrayList<>();

    public void addFlightList(List<Flight> flightList){
        if(flightList != null){
            flightList.forEach(this::addFlight);
        }
    }

    public void addFlight(Flight flight) {
        if(flight != null){
            flightList.add(flight);
            flight.setBooking(this);
        }
    }
}
