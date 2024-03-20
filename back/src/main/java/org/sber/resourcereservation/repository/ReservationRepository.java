package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * Репозиторий для работы с резервами.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Находит все бронирования по заданному пользователю.
     *
     * @param user Пользователь, для которого осуществляется поиск бронирований.
     * @return Список резервов пользователя.
     */
    List<Reservation> findAllByUser(User user);

    /**
     * Находит все бронирования по заданному ресурсу.
     *
     * @param resource Ресурс, для которого осуществляется поиск бронирований.
     * @return Список резервов ресурса.
     */
    List<Reservation> findAllByResource(Resource resource);

    /**
     * Находит все бронирования, которые происходят в заданное время.
     *
     * @param start Время начала периода бронирования.
     * @return Список резервов, которые происходят в заданное время.
     */
    @Query("select r from reservations r where :time between r.reservationStart and r.reservationEnd")
    List<Reservation> findAllByTime(@Param("time") Timestamp start);

    /**
     * Проверяет, свободен ли ресурс в заданный период времени.
     *
     * @param start    Время начала периода бронирования.
     * @param end      Время окончания периода бронирования.
     * @param resource Ресурс, для которого проверяется доступность.
     * @return true, если ресурс свободен в заданный период времени, иначе false.
     */
    @Query("select count (r) = 0 from reservations r where r.resource = :resource and not (:end <= r.reservationStart or :start >= r.reservationEnd)")
    boolean isResourceFreeInPeriod(@Param("start") Timestamp start, @Param("end") Timestamp end, @Param("resource") Resource resource);
}
