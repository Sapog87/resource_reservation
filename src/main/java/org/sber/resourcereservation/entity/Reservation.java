package org.sber.resourcereservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
}
