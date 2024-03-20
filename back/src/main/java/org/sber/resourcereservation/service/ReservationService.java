package org.sber.resourcereservation.service;

import jakarta.servlet.http.HttpServletRequest;
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
import java.util.List;
import java.util.Objects;

/**
 * Сервисный класс для работы с резервами.
 */
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

    /**
     * Находит резерв по его идентификатору.
     *
     * @param id Идентификатор бронирования.
     * @return Объект бронирования.
     * @throws ReservationNotFoundException Если бронирование с указанным идентификатором не найдено.
     */
    public Reservation findById(Long id) {
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("No reservation with such id"));
    }

    /**
     * Находит список бронирований для указанного пользователя.
     *
     * @param name Имя пользователя.
     * @return Список резервов пользователя.
     * @throws UserNotFoundException        Если пользователь с указанным именем не найден.
     * @throws ReservationNotFoundException Если у пользователя нет резервов.
     */
    public List<Reservation> findByUserName(String name) {
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

    /**
     * Находит список бронирований для указанного ресурса.
     *
     * @param name Имя ресурса.
     * @return Список резервов ресурса.
     * @throws ResourceNotFoundException    Если ресурс с указанным именем не найден.
     * @throws ReservationNotFoundException Если ресурс не имеет резервов.
     */
    public List<Reservation> findByResourceName(String name) {
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

    /**
     * Находит список резервов для указанного времени.
     *
     * @param date Время бронирования.
     * @return Список резервов для указанного времени.
     * @throws ReservationNotFoundException Если нет бронирований на указанное время.
     */
    public List<Reservation> findByTime(Timestamp date) {
        List<Reservation> reservations = reservationRepository.findAllByTime(date);
        if (!reservations.isEmpty()) {
            return reservations;
        } else {
            throw new ReservationNotFoundException("No reservations at specified time");
        }
    }

    /**
     * Пытается создать резерв для указанного временного периода.
     *
     * @param start    Начальное время бронирования.
     * @param end      Конечное время бронирования.
     * @param resource Ресурс, который требуется зарезервировать.
     * @param user     Пользователь, который осуществляет резервирование.
     * @return Созданное бронирование.
     * @throws InvalidPeriodException Если указанный период занят другим резервом.
     */
    @Retryable(retryFor = SQLException.class, maxAttempts = 5, backoff = @Backoff(delay = 1500, multiplier = 3.0, random = true))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Reservation makeReservation(Timestamp start, Timestamp end, Resource resource, User user) {
        boolean isFree = reservationRepository.isResourceFreeInPeriod(start, end, resource);
        if (!isFree)
            throw new InvalidPeriodException("Specified period has collisions with other reservations");

        Reservation reservation = new Reservation(user, resource, start, end);
        reservationRepository.save(reservation);
        return reservation;
    }

    /**
     * Метод release освобождает бронирование с указанным идентификатором.
     *
     * @param id      Идентификатор бронирования, которое нужно освободить.
     * @param request Объект HttpServletRequest для проверки пользователя.
     * @return true, если бронирование было успешно освобождено.
     * @throws ReservationNotFoundException Если не найдено бронирование с указанным идентификатором.
     * @throws InvalidUserException         Если текущий пользователь не является владельцем бронирования.
     */
    @Retryable(retryFor = SQLException.class)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean release(Long id, HttpServletRequest request) {
        Reservation reservation = reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("No reservations with such id"));
        String userName = reservation.getUser().getName();
        String userPrincipal = request.getUserPrincipal().getName();
        if (!Objects.equals(userName, userPrincipal)) {
            throw new InvalidUserException("You can't release reservation of another user");
        }
        reservationRepository.delete(reservation);
        return true;
    }

    /**
     * Возвращает список всех бронирований.
     *
     * @return Список всех бронирований.
     * @throws ReservationNotFoundException Если не найдено ни одного бронирования.
     */
    public List<Reservation> all() {
        List<Reservation> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("No reservations was found");
        }
        return reservations;
    }
}
