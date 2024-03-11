package org.sber.resourcereservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class AcquireDto {
    private UserDto user;
    private ResourceDto resource;
    private Timestamp start;
    private Timestamp end;
}
