package org.sber.resourcereservation.exception;

/**
 * Исключение, выбрасываемое при попытке создать ресурс, который уже существует.
 * Используется для обработки ситуаций, когда пытаются создать ресурс с уже существующим именем.
 */
public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException() {
    }

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
