package org.sber.resourcereservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Wrapper<T> {
    private T value;

    public Wrapper() {
    }
}
