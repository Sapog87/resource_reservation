package org.sber.resourcereservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity(name = "resources")
@Table(indexes = @Index(columnList = "name"))
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resources_seq")
    @SequenceGenerator(name = "resources_seq", sequenceName = "resources_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "resource")
    private List<Reservation> reservations;
}
