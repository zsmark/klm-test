package com.klm.test.klm_test.dto;

import java.util.List;

public interface FlightValueDto {
    AirportCode getDepartureAirport();
    AirportCode getArrivalAirport();

    default List<String> getAirports(){
        return List.of(getDepartureAirport().getIataCode(), getArrivalAirport().getIataCode());
    }

    interface AirportCode {
        String getIataCode();
    }
}
