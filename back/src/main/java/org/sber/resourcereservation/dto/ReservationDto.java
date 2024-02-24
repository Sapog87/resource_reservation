package org.sber.resourcereservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReservationDto {
    private Long id;
    private Date reservationStart;
    private Date reservationEnd;
    private UserDto user;
    private ResourceDto resource;
}
