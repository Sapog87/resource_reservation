package org.sber.resourcereservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для передачи информации о пользователе.
 * Используется для передачи имени пользователя.
 */
@Getter
@Setter
public class UserDto {
    @NotBlank
    private String name;
}
