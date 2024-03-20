package org.sber.resourcereservation.exception;

/**
 * Исключение, выбрасываемое при отсутствии указанного ресурса.
 * Используется для обработки ситуаций, когда запрашиваемый ресурс не найден.
 */
public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
