package org.sber.resourcereservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для идентификатора.
 * Используется для передачи идентификатора сущности.
 */
@AllArgsConstructor
@Getter
@Setter
public class Id {
    private Long id;
    public Id() {}
}
