package org.sber.resourcereservation.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.sber.resourcereservation.entity.Role;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.UserAlreadyExistException;
import org.sber.resourcereservation.exception.UserNotFoundException;
import org.sber.resourcereservation.repository.RoleRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Сервисный класс для работы с пользователями.
 */
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

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param user    Новый пользователь для регистрации.
     * @param request Запрос HTTP.
     * @return True, если регистрация выполнена успешно, иначе false.
     * @throws UserAlreadyExistException Если пользователь с таким именем уже существует.
     */
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

    /**
     * Пытается войти в систему от имени пользователя с заданными учетными данными.
     *
     * @param user    Пользователь, пытающийся войти в систему.
     * @param request Запрос HTTP.
     * @return True, если вход выполнен успешно.
     */
    public Boolean login(User user, HttpServletRequest request) {
        try {
            request.logout();
            request.login(user.getName(), user.getPassword());
            return true;
        } catch (ServletException e) {
            throw new RuntimeException(e.getCause().getMessage(), e.getCause());
        }
    }

    /**
     * Получает список всех пользователей.
     *
     * @return Список всех пользователей.
     * @throws UserNotFoundException Если не найдены пользователи.
     */
    public List<User> all() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users was found");
        }
        return users;
    }
}
