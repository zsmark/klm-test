package com.klm.test.klm_test.repository;

import com.klm.test.klm_test.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, String> {
}
