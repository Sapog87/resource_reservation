package org.sber.resourcereservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthDto {
    @NotBlank
    private String name;
    @NotBlank
    private String password;
}
