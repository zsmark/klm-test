package com.klm.test.klm_test.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class FlightsDto {
    @NotEmpty
    private String departureAirport;
    @NotEmpty
    private String arrivalAirport;
}
