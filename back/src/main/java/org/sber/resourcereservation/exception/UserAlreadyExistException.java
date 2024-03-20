package org.sber.resourcereservation.exception;

/**
 * Исключение, выбрасываемое при попытке создать пользователя, который уже существует.
 * Используется для обработки ситуаций, когда пытаются создать пользователь с уже существующим именем.
 */
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
