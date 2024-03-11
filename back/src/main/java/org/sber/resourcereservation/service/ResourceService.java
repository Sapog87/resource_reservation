package org.sber.resourcereservation.service;

import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.*;
import org.sber.resourcereservation.repository.ResourceRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ResourceService {
    private final ReservationService reservationService;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ResourceService(ReservationService reservationService, ResourceRepository resourceRepository, UserRepository userRepository) {
        this.reservationService = reservationService;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    public Long acquire(User user, Resource resource, Timestamp start, Timestamp end) {
        User u = validateUser(user);
        Resource r = valiadateResource(resource);

        if (start.after(end))
            throw new InvalidPeriodException("Duration can't be negative");

        Reservation reservation = reservationService.getReservation(start, end, r, u);

        return reservation.getId();
    }

    private Resource valiadateResource(Resource resource) {
        String resourceName = resource.getName();
        Resource r = resourceRepository.findByName(resourceName);
        if (Objects.isNull(r))
            throw new ResourceNotFoundException("No resource with such name");
        return r;
    }

    private User validateUser(User user) {
        String userName = user.getName();
        User u = userRepository.findByName(userName);
        if (Objects.isNull(u))
            throw new UserNotFoundException("No user with name");
        return u;
    }

    public List<Resource> all() {
        List<Resource> resources = resourceRepository.findAll();
        if (resources.isEmpty()){
            throw new ReservationNotFoundException("No resources was found");
        }
        return resources;
    }

    @Transactional
    public Boolean create(Resource resource) {
        if (Objects.nonNull(resourceRepository.findByName(resource.getName()))) {
            throw new ResourceAlreadyExistException("Resource with such name already exist");
        }
        resourceRepository.save(resource);
        return true;
    }
}
