package com.klm.test.klm_test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="No such airport")
public class AirportDoesNotExistsException extends RuntimeException {
    public AirportDoesNotExistsException(String message) {
        super(message);
    }
}
