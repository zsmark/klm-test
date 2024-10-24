package com.klm.test.klm_test.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class CreateBookingDto {
    @NotEmpty
    private String paxName;

    @NotNull
    private ZonedDateTime departure;

    @NotEmpty
    @Valid
    private List<FlightsDto> flightsDtoList;
}
