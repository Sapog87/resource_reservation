package org.sber.resourcereservation.advice;

import org.sber.resourcereservation.exception.InvalidPeriodException;
import org.sber.resourcereservation.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice("org.sber.resourcereservation.controller")
public class RestExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<CustomErrorResponse> notFound(NotFoundException e, WebRequest request) {
        CustomErrorResponse errorResponse = response(HttpStatus.NOT_FOUND, request, e);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPeriodException.class)
    protected ResponseEntity<Object> invalidPeriod(InvalidPeriodException e, WebRequest request) {
        CustomErrorResponse errorResponse = response(HttpStatus.CONFLICT, request, e);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    private CustomErrorResponse response(HttpStatus status, WebRequest request, Exception e) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setError(e.getMessage());
        errorResponse.setPath(request.getDescription(false));
        return errorResponse;
    }
}
