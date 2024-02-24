package org.sber.resourcereservation.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.sber.resourcereservation.dto.ReservationDto;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final ModelMapper modelMapper;

    public ReservationController(ReservationService reservationService, ModelMapper modelMapper) {
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ReservationDto findById(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return modelMapper.map(reservation, ReservationDto.class);
    }

    @GetMapping
    public List<ReservationDto> findByTime(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
        List<Reservation> reservations = reservationService.findByTime(Timestamp.valueOf(localDateTime));
        return modelMapper.map(reservations, new TypeToken<List<ReservationDto>>() {}.getType());
    }
}
