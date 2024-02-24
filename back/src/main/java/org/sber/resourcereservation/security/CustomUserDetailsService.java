package org.sber.resourcereservation.security;

import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.UserNotFoundException;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("No user with name: {%s}".formatted(name));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                null
                //SecurityHelper.getAuthorities(List.of(user.getAuthorities()))
        );
    }
}
