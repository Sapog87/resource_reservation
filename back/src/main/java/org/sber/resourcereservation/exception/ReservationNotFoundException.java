package org.sber.resourcereservation.exception;

/**
 * Исключение, выбрасываемое при отсутствии указанного бронирования.
 * Используется для обработки ситуаций, когда запрашиваемое бронирование не найдено.
 */
public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException() {
    }

    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationNotFoundException(Throwable cause) {
        super(cause);
    }
}
