package org.sber.resourcereservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
public class AcquireDto {
    private UserDto user;
    private ResourceDto resource;
    private LocalDateTime start;
    private Duration duration;
}
