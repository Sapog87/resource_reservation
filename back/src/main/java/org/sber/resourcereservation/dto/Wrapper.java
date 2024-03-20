package org.sber.resourcereservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Обертка для передачи значения.
 * Используется для упаковки значения для передачи в виде DTO.
 */
@Getter
@Setter
@AllArgsConstructor
public class Wrapper<T> {
    private T value;

    public Wrapper() {
    }
}
