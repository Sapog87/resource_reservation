package org.sber.resourcereservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceDto {
    @NotBlank
    private String name;
}
