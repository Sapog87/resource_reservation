package org.sber.resourcereservation.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.sber.resourcereservation.dto.*;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.service.ReservationService;
import org.sber.resourcereservation.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final ReservationService reservationService;
    private final ModelMapper modelMapper;

    public ResourceController(ResourceService resourceService, ReservationService reservationService, ModelMapper modelMapper) {
        this.resourceService = resourceService;
        this.reservationService = reservationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/acquire")
    public Id acquire(@RequestBody AcquireDto acquire) {
        User user;
        Resource resource;
        Timestamp start;
        Timestamp end;
        try {
            user = modelMapper.map(acquire.getUser(), User.class);
            resource = modelMapper.map(acquire.getResource(), Resource.class);
            start = acquire.getStart();
            end = acquire.getEnd();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new Id(resourceService.acquire(user, resource, start, end));
    }

    @GetMapping(value = "/{name}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDto> findByResource(@PathVariable String name) {
        List<Reservation> reservations = reservationService.findByResource(name);
        return modelMapper.map(reservations, new TypeToken<List<ReservationDto>>() {}.getType());
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Wrapper<Boolean> create(@RequestBody ResourceDto resourceDto) {
        Resource resource;
        try {
            resource = modelMapper.map(resourceDto, Resource.class);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new Wrapper<>(resourceService.create(resource));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResourceDto> all() {
        return modelMapper.map(resourceService.all(), new TypeToken<List<ResourceDto>>() {}.getType());
    }
}
