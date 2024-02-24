package org.sber.resourcereservation;

import org.sber.resourcereservation.exception.InvalidPeriodException;
import org.sber.resourcereservation.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("org.sber.resourcereservation.controller")
public class RestExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> notFound(NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(InvalidPeriodException.class)
    protected ResponseEntity<Object> invalidPeriod(InvalidPeriodException e) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);

    }
}
