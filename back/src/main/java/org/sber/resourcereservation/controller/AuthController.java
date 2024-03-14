package org.sber.resourcereservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.sber.resourcereservation.dto.UserAuthDto;
import org.sber.resourcereservation.dto.Wrapper;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AuthController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Wrapper<Boolean> authenticateUser(@Valid @RequestBody UserAuthDto userDto, HttpServletRequest request) {
        User user = modelMapper.map(userDto, User.class);
        return new Wrapper<>(userService.login(user, request));
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Wrapper<Boolean> registerUser(@Valid @RequestBody UserAuthDto userDto, HttpServletRequest request) {
        User user = modelMapper.map(userDto, User.class);
        return new Wrapper<>(userService.signup(user, request));
    }
}
