package org.sber.resourcereservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.sber.resourcereservation.dto.ReservationDto;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/**
 * Контроллер для работы с резервами.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final ModelMapper modelMapper;

    public ReservationController(ReservationService reservationService, ModelMapper modelMapper) {
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/release/{id}")
    public Boolean release(@PathVariable @Pattern(regexp = "\\d+") String id, HttpServletRequest request) {
        long longId = parsePathVariable(id);
        return reservationService.release(longId, request);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationDto findById(@PathVariable @Pattern(regexp = "\\d+") String id) {
        long longId = parsePathVariable(id);
        Reservation reservation = reservationService.findById(longId);
        return modelMapper.map(reservation, ReservationDto.class);
    }

    private long parsePathVariable(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be of {Long} type");
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDto> findByTime(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
        List<Reservation> reservations;
        if (Objects.isNull(localDateTime)) {
            reservations = reservationService.all();
        } else {
            Timestamp timestamp = new Timestamp(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
            reservations = reservationService.findByTime(timestamp);
        }
        return modelMapper.map(reservations, new TypeToken<List<ReservationDto>>() {}.getType());
    }
}
