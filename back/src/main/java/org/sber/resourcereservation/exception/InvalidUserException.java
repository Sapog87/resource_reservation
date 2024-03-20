package org.sber.resourcereservation.exception;

/**
 * Исключение, выбрасываемое при недопустимом пользователе.
 * Используется для обработки ситуаций, когда указанный пользователь недействителен.
 */
public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
    }

    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserException(Throwable cause) {
        super(cause);
    }
}
