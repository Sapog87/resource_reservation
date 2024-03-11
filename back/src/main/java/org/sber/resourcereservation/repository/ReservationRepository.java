package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser(User user);

    List<Reservation> findAllByResource(Resource resource);

    @Query("select r from reservations r where :time between r.reservationStart and r.reservationEnd")
    List<Reservation> findAllByTime(@Param("time") Date start);

    @Query("select count (r) = 0 " +
            "from reservations r " +
            "where r.resource = :resource " +
            "and not (:end <= r.reservationStart or :start >= r.reservationEnd)")
    boolean isResourceFreeInPeriod(@Param("start") Date start, @Param("end") Date end, @Param("resource") Resource resource);
}
