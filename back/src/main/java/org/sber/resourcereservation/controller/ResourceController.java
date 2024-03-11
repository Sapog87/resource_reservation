package org.sber.resourcereservation.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.sber.resourcereservation.dto.AcquireDto;
import org.sber.resourcereservation.dto.Id;
import org.sber.resourcereservation.dto.ReservationDto;
import org.sber.resourcereservation.dto.ResourceDto;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.service.ReservationService;
import org.sber.resourcereservation.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@CrossOrigin
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
        Date start;
        Date end;
        try {
            user = modelMapper.map(acquire.getUser(), User.class);
            resource = modelMapper.map(acquire.getResource(), Resource.class);
            start = Timestamp.valueOf(modelMapper.map(acquire.getStart(), LocalDateTime.class));
            end = Timestamp.valueOf(modelMapper.map(acquire.getEnd(), LocalDateTime.class));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return new Id(resourceService.acquire(user, resource, start, end));
    }

    @PostMapping("/release/{id}")
    public Boolean release(@PathVariable Long id) {
        return reservationService.release(id);
    }

    @GetMapping("/{name}/reservations")
    public List<ReservationDto> findByResource(@PathVariable String name) {
        List<Reservation> reservations = reservationService.findByResource(name);
        return modelMapper.map(reservations, new TypeToken<List<ReservationDto>>() {}.getType());
    }

    @PostMapping("/create")
    public Boolean create(@RequestBody ResourceDto resourceDto) {
        Resource resource;
        try {
            resource = modelMapper.map(resourceDto, Resource.class);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return reservationService.create(resource);
    }

    @GetMapping
    public List<Resource> all(){
        return resourceService.all();
    }
}
