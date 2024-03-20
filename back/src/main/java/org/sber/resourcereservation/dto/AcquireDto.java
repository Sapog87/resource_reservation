package org.sber.resourcereservation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * DTO для резервирования ресурса.
 * Используется для передачи информации о пользователе, ресурсе и временных рамках бронирования.
 */
@Getter
@Setter
public class AcquireDto {
    @Valid
    @NotNull
    private UserDto user;
    @Valid
    @NotNull
    private ResourceDto resource;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime end;
}
