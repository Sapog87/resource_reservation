package org.sber.resourcereservation.exception;

public class InvalidPeriodException extends RuntimeException {
    public InvalidPeriodException() {
    }

    public InvalidPeriodException(String message) {
        super(message);
    }

    public InvalidPeriodException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPeriodException(Throwable cause) {
        super(cause);
    }
}
