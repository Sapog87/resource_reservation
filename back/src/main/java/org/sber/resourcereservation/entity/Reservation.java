package org.sber.resourcereservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservations_seq")
    @SequenceGenerator(name = "reservations_seq", sequenceName = "reservations_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Date reservationStart;

    @Column
    private Date reservationEnd;

    @ManyToOne
    private User user;

    @ManyToOne
    private Resource resource;

    public Reservation(User user, Resource resource, Date reservationStart, Date reservationEnd) {
        this.user = user;
        this.resource = resource;
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
    }

    public Reservation() {

    }
}
