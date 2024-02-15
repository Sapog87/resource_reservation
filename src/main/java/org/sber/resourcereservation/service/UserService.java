package org.sber.resourcereservation.service;

import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {this.repository = repository;}
}
