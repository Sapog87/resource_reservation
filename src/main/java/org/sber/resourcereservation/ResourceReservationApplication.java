package org.sber.resourcereservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ResourceReservationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ResourceReservationApplication.class, args);
    }

}