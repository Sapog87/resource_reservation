package org.sber.resourcereservation.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.sber.resourcereservation.dto.ReservationDto;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.service.ReservationService;
import org.sber.resourcereservation.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final ReservationService reservationService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserController(ReservationService reservationService, ModelMapper modelMapper, UserService userService) {
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/{name}/reservations")
    public List<ReservationDto> findReservationsByUser(@PathVariable String name) {
        List<Reservation> reservations = reservationService.findByUser(name);
        return modelMapper.map(reservations, new TypeToken<List<ReservationDto>>() {}.getType());
    }

    @GetMapping
    public List<User> all() {
        return userService.all();
    }

}
