package org.sber.resourcereservation.service;

import jakarta.transaction.Transactional;
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
@Transactional
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
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("No reservation with such id"));
    }

    public List<Reservation> findByUser(String name) {
        User user = userRepository.findByName(name);
        if (Objects.nonNull(user)) {
            var reservations = reservationRepository.findAllByUser(user);
            if (!reservations.isEmpty()) {
                return reservations;
            } else {
                throw new ReservationNotFoundException("User with such name has no reservations");
            }
        } else {
            throw new UserNotFoundException("No user with such name");
        }
    }

    public List<Reservation> findByResource(String name) {
        Resource resource = resourceRepository.findByName(name);
        if (Objects.nonNull(resource)) {
            var reservations = reservationRepository.findAllByResource(resource);
            if (!reservations.isEmpty()) {
                return reservations;
            } else {
                throw new ReservationNotFoundException("Resource with such name has no reservations");
            }
        } else {
            throw new ResourceNotFoundException("No resource with such name");
        }
    }

    public List<Reservation> findByTime(Date date) {
        List<Reservation> reservations = reservationRepository.findAllByTime(date);
        if (!reservations.isEmpty()) {
            return reservations;
        } else {
            throw new ReservationNotFoundException("No reservations at specified time");
        }
    }
}
