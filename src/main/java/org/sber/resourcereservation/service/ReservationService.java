package org.sber.resourcereservation.service;

import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.ReservationNotFoundException;
import org.sber.resourcereservation.exception.ResourceNotFoundException;
import org.sber.resourcereservation.exception.UserNotFoundException;
import org.sber.resourcereservation.repository.ReservationRepository;
import org.sber.resourcereservation.repository.ResourceRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    private final ResourceRepository resourceRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationService(ResourceRepository resourceRepository, ReservationRepository reservationRepository, UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException("no such reservation with id: {%d}".formatted(id)));
    }

    public List<Reservation> findByUser(User user) {
        String name = user.getName();
        User u = userRepository.findByName(name);
        if (Objects.nonNull(u)) {
            return reservationRepository.findAllByUser(u);
        } else {
            throw new UserNotFoundException("no such reservation with name: {%s}".formatted(name));
        }
    }

    public List<Reservation> findByResource(Resource resource) {
        String name = resource.getName();
        Resource r = resourceRepository.findByName(name);
        if (Objects.nonNull(r)) {
            return reservationRepository.findAllByResource(r);
        } else {
            throw new ResourceNotFoundException("no such resource with name: {%s}".formatted(name));
        }
    }

    public List<Reservation> findByTime(Date date) {
        //TODO
        return null;
    }
}
