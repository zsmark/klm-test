package com.klm.test.klm_test.service.impl;

import com.klm.test.klm_test.exception.AirportDoesNotExistsException;
import com.klm.test.klm_test.model.Airport;
import com.klm.test.klm_test.repository.AirportRepository;
import com.klm.test.klm_test.service.AirportService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    private Map<String,Airport> airports;

    @PostConstruct
    public void init() {
        log.info("Caching iata codes");
        airports = airportRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Airport::getIataCode, airport -> airport));
        log.info("Iata codes cache: {}", airports);
    }

    @Override
    public Airport getAirportByCode(String code) {
        if(airports.containsKey(code)) {
            return airports.get(code);
        }
        throw new AirportDoesNotExistsException(String.format("Specified airport %s does not exists!", code));
    }
}
