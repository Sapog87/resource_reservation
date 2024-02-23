package org.sber.resourcereservation.controller;

import org.modelmapper.ModelMapper;
import org.sber.resourcereservation.dto.AcquireDto;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final ModelMapper modelMapper;

    public ResourceController(ResourceService resourceService, ModelMapper modelMapper) {
        this.resourceService = resourceService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/acquire")
    public Long acquire(@RequestBody AcquireDto acquire) {
        User user = modelMapper.map(acquire.getUser(), User.class);
        Resource resource = modelMapper.map(acquire.getResource(), Resource.class);
        Date start = Timestamp.valueOf(modelMapper.map(acquire.getStart(), LocalDateTime.class));
        Duration duration = modelMapper.map(acquire.getDuration(), Duration.class);
        return resourceService.acquire(user, resource, start, duration);
    }

    @PostMapping("/release/{id}")
    public Boolean release(@PathVariable Long id) {
        return resourceService.release(id);
    }
}
