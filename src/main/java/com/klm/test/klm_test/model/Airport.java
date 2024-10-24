package com.klm.test.klm_test.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@ToString
public class Airport {

    @Id
    @NotEmpty
    @Size(min = 3, max = 3)
    private String iataCode;

    @NotEmpty
    private String displayName;

}
