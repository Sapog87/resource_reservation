package org.sber.resourcereservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Является точкой входа Spring Boot приложения.
 */
@SpringBootApplication
@EnableRetry
public class ResourceReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourceReservationApplication.class, args);
    }
}
