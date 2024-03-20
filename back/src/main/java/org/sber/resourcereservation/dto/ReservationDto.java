package org.sber.resourcereservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * DTO для передачи информации о резерве.
 * Используется для передачи идентификатора бронирования, времени начала и окончания бронирования,
 * а также сведений о пользователе и ресурсе, связанных с бронированием.
 */
@Getter
@Setter
public class ReservationDto {
    private Long id;
    private Date reservationStart;
    private Date reservationEnd;
    private UserDto user;
    private ResourceDto resource;
}
