package org.sber.resourcereservation.service;

import jakarta.transaction.Transactional;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.InvalidPeriodException;
import org.sber.resourcereservation.exception.ReservationNotFoundException;
import org.sber.resourcereservation.exception.ResourceNotFoundException;
import org.sber.resourcereservation.exception.UserNotFoundException;
import org.sber.resourcereservation.repository.ReservationRepository;
import org.sber.resourcereservation.repository.ResourceRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public ResourceService(ResourceRepository resourceRepository, UserRepository userRepository, ReservationRepository reservationRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    public Long acquire(User user, Resource resource, Date start, Duration duration) {
        String userName = user.getName();
        User u = userRepository.findByName(userName);
        if (Objects.isNull(u))
            throw new UserNotFoundException("No user with name: {%s}".formatted(userName));

        String resourceName = resource.getName();
        Resource r = resourceRepository.findByName(resourceName);
        if (Objects.isNull(r))
            throw new ResourceNotFoundException("No resource with name: {%s}".formatted(resourceName));

        if (duration.isNegative())
            throw new IllegalArgumentException("Duration can't be negative");

        Date end = new Date(start.getTime() + duration.toMillis());

        boolean isFree = reservationRepository.isResourceFreeInPeriod(start, end, r);
        if (!isFree)
            throw new InvalidPeriodException("Specified period has collisions with other reservations");

        Reservation reservation = new Reservation();
        reservation.setResource(r);
        reservation.setUser(u);
        reservation.setReservationStart(start);
        reservation.setReservationEnd(end);

        reservationRepository.save(reservation);

        return reservation.getId();
    }

    public boolean release(Long id) {
        Reservation reservation = reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("no reservations with id: {%d}".formatted(id)));
        reservationRepository.delete(reservation);
        return true;
    }
}
