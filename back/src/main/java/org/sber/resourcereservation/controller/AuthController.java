package org.sber.resourcereservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.sber.resourcereservation.dto.UserAuthDto;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
public class AuthController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AuthController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public Boolean authenticateUser(@RequestBody UserAuthDto userDto, HttpServletRequest request) {
        User user;
        try {
            user = modelMapper.map(userDto, User.class);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return userService.login(user, request);
    }

    @PostMapping("/signup")
    public Boolean registerUser(@RequestBody UserAuthDto userDto, HttpServletRequest request) {
        User user;
        try {
            user = modelMapper.map(userDto, User.class);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return userService.signup(user, request);
    }
}
