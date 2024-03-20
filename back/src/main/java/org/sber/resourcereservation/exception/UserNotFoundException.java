package org.sber.resourcereservation.exception;

/**
 * Исключение, выбрасываемое при отсутствии указанного пользователя.
 * Используется для обработки ситуаций, когда запрашиваемый пользователь не найден.
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String s) {
        super(s);
    }
}
