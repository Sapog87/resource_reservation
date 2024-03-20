package org.sber.resourcereservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для передачи информации о ресурсе.
 * Используется для передачи имени ресурса.
 */
@Getter
@Setter
public class ResourceDto {
    @NotBlank
    private String name;
}
