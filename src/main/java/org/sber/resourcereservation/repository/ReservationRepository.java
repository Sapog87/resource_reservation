package org.sber.resourcereservation.repository;

import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser(User user);

    List<Reservation> findAllByResource(Resource resource);

    List<Reservation> findAllByReservationStartIsBeforeAndReservationEndIsAfter(Date start, Date end);

}
