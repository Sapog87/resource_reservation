package org.sber.resourcereservation.security;

import org.sber.resourcereservation.entity.Role;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.UserNotFoundException;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = userRepository.findByNameWithRoles(name);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("No user with such name");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                getAuthorities(user.getAuthorities())
        );
    }

    private static List<GrantedAuthority> getAuthorities(Collection<Role> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role auth : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(auth.getName()));
        }
        return grantedAuthorities;
    }
}
