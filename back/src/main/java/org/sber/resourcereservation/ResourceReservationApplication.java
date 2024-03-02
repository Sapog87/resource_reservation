package org.sber.resourcereservation;

import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.repository.ReservationRepository;
import org.sber.resourcereservation.repository.ResourceRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.sber.resourcereservation.service.ResourceService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.time.Duration;

@SpringBootApplication
public class ResourceReservationApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(ResourceReservationApplication.class, args);

        var resourceService = context.getBean(ResourceService.class);
        var userRepository = context.getBean(UserRepository.class);
        var resourceRepository = context.getBean(ResourceRepository.class);
        var reservationRepository = context.getBean(ReservationRepository.class);

        User user = new User();
        user.setName("aaa");
        user.setPassword("aaa");
        user = userRepository.save(user);

        Resource resource = new Resource();
        resource.setName("aaa");
        resource = resourceRepository.save(resource);

        Long id = resourceService.acquire(user, resource, new Date(0), new Date(10000));

        System.out.println(reservationRepository.findAll().size());
    }

}
