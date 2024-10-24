package com.klm.test.klm_test.repository;

import com.klm.test.klm_test.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
