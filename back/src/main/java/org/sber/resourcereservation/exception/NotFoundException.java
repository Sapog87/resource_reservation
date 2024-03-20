package org.sber.resourcereservation.exception;

/**
 * Исключение, выбрасываемое при отсутствии указанной записи.
 * Является суперклассом для ряда других исключений.
 * Используется для обработки ситуаций, когда запрашивая запись не найдена.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
