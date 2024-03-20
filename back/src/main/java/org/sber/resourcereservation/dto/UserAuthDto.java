package org.sber.resourcereservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для аутентификации пользователя.
 * Используется для передачи имени пользователя и пароля.
 */
@Getter
@Setter
public class UserAuthDto {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
}
