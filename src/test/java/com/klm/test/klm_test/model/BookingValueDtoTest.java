package com.klm.test.klm_test.model;

import com.klm.test.klm_test.dto.BookingValueDto;
import com.klm.test.klm_test.dto.FlightValueDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
public class BookingValueDtoTest implements BookingValueDto {
    private Long id;
    private String paxName;
    private Instant departure;
    private String itinerary;

    @Override
    public String getItinerary(){
        return itinerary;
    }

    @Override
    public List<FlightValueDto> getFlightList() {
        return List.of();
    }
}
