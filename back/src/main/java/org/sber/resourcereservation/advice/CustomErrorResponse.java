package org.sber.resourcereservation.advice;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс используется для создания пользовательского ответа об ошибке в приложении.
 */
@Getter
@Setter
public class CustomErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String path;
}
