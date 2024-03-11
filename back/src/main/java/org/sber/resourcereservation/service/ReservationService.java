package org.sber.resourcereservation.service;

import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.*;
import org.sber.resourcereservation.repository.ReservationRepository;
import org.sber.resourcereservation.repository.ResourceRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
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

    public List<Reservation> findByTime(Timestamp date) {
        List<Reservation> reservations = reservationRepository.findAllByTime(date);
        if (!reservations.isEmpty()) {
            return reservations;
        } else {
            throw new ReservationNotFoundException("No reservations at specified time");
        }
    }

    @Retryable(retryFor = SQLException.class, maxAttempts = 5, backoff = @Backoff(delay = 1500, multiplier = 3.0, random = true))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Reservation getReservation(Timestamp start, Timestamp end, Resource r, User u) {
        boolean isFree = reservationRepository.isResourceFreeInPeriod(start, end, r);
        if (!isFree)
            throw new InvalidPeriodException("Specified period has collisions with other reservations");

        Reservation reservation = new Reservation(u, r, start, end);
        reservationRepository.save(reservation);
        return reservation;
    }

    @Retryable(retryFor = SQLException.class)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean release(Long id) {
        Reservation reservation = reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("No reservations with such id"));
        System.out.println("aaa");
        reservationRepository.delete(reservation);
        return true;
    }


    public List<Reservation> all() {
        List<Reservation> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("No reservations was found");
        }
        return reservations;
    }
}
