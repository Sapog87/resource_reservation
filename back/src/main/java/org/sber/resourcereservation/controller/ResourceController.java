package org.sber.resourcereservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.sber.resourcereservation.dto.*;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.service.ReservationService;
import org.sber.resourcereservation.service.ResourceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Контроллер для работы с ресурсами.
 */
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

    @PostMapping(value = "/acquire", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Id acquire(@Valid @RequestBody AcquireDto acquire, HttpServletRequest request) {
        User user = modelMapper.map(acquire.getUser(), User.class);
        Resource resource = modelMapper.map(acquire.getResource(), Resource.class);
        Timestamp start = Timestamp.valueOf(acquire.getStart().atZone(ZoneId.of("GMT")).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        Timestamp end = Timestamp.valueOf(acquire.getEnd().atZone(ZoneId.of("GMT")).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        return new Id(resourceService.acquire(user, resource, start, end, request));
    }

    @GetMapping(value = "/{name}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationDto> findByResource(@PathVariable String name) {
        List<Reservation> reservations = reservationService.findByResourceName(name);
        return modelMapper.map(reservations, new TypeToken<List<ReservationDto>>() {}.getType());
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Wrapper<Boolean> create(@Valid @RequestBody ResourceDto resourceDto) {
        Resource resource = modelMapper.map(resourceDto, Resource.class);
        return new Wrapper<>(resourceService.create(resource));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResourceDto> all() {
        return modelMapper.map(resourceService.all(), new TypeToken<List<ResourceDto>>() {}.getType());
    }
}
