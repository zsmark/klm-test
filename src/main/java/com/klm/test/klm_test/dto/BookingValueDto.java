package com.klm.test.klm_test.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public interface BookingValueDto {
    Long getId();
    String getPaxName();
    Instant getDeparture();
    @JsonIgnore
    List<FlightValueDto> getFlightList();

    default String getItinerary(){
        return getFlightList()
                .stream()
                .flatMap((FlightValueDto flightValueDto) -> flightValueDto.getAirports().stream())
                .distinct()
                .collect(Collectors.joining("-"));
    }
}
