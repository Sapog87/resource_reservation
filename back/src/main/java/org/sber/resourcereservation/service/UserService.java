package org.sber.resourcereservation.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.sber.resourcereservation.entity.Role;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.ReservationNotFoundException;
import org.sber.resourcereservation.exception.UserAlreadyExistException;
import org.sber.resourcereservation.exception.UserNotFoundException;
import org.sber.resourcereservation.repository.RoleRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Boolean login(User user, HttpServletRequest request) {
        try {
            request.logout();
            request.login(user.getName(), user.getPassword());
            return true;
        } catch (ServletException e) {
            throw new RuntimeException(e.getCause().getMessage(), e.getCause());
        }
    }

    public Boolean signup(User user, HttpServletRequest request) {
        if (userRepository.existsByName(user.getName())) {
            throw new UserAlreadyExistException("User with such name already exist");
        }
        User u = new User();
        u.setName(user.getName());
        u.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName("USER");
        u.setAuthorities(Set.of(role));
        userRepository.save(u);
        login(user, request);
        return true;
    }

    public List<User> all() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users was found");
        }
        return users;
    }
}
