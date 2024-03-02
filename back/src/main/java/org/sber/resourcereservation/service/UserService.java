package org.sber.resourcereservation.service;

import jakarta.transaction.Transactional;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.UserNotFoundException;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {this.repository = repository;}

    public User findUserById(String name) {
        User user = repository.findByName(name);
        if (Objects.isNull(user))
            throw new UserNotFoundException("No user with such name");
        return user;
    }
}
