package com.klm.test.klm_test.model;

import com.klm.test.klm_test.dto.FlightValueDto;
import lombok.Data;

@Data
public class FlightValueDtoTest implements FlightValueDto {
    private AirportCode departureAirport;
    private AirportCode arrivalAirport;
}
