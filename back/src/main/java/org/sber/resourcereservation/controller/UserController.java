package org.sber.resourcereservation.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.sber.resourcereservation.dto.ReservationDto;
import org.sber.resourcereservation.dto.UserDto;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.service.ReservationService;
import org.sber.resourcereservation.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final ReservationService reservationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(ReservationService reservationService, UserService userService, ModelMapper modelMapper) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{name}/reservations")
    public List<ReservationDto> findReservationsByUser(@PathVariable String name) {
        List<Reservation> reservations = reservationService.findByUser(name);
        return modelMapper.map(reservations, new TypeToken<List<ReservationDto>>() {}.getType());
    }

    @GetMapping("/{name}")
    public UserDto findUserById(@PathVariable String name) {
        User user = userService.findUserById(name);
        return modelMapper.map(user, UserDto.class);
    }

}
