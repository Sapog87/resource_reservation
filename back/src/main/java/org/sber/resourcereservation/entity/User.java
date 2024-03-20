package org.sber.resourcereservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * Сущность для хранения информации о пользователе.
 * Используется для отображения таблицы "users" в базе данных.
 */
@Getter
@Setter
@Entity(name = "users")
@Table(indexes = @Index(columnList = "name"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    @ManyToMany
    private Set<Role> authorities;
}
