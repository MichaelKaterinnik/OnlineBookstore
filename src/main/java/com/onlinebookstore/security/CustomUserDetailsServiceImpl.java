package com.onlinebookstore.security;

import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.services.UsersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Клас, який реалізує пошук користувача при його авторизації та визначення його ролі.
 */

@Component
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    @Autowired
    private UsersService usersService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = null;
        try {
            user = usersService.findByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }

        return new User(user.getEmail(), user.getPassword(), getAuthority(String.valueOf(user.getRole())));
    }

    private Collection<? extends GrantedAuthority> getAuthority(String role) {
        return List.of(new SimpleGrantedAuthority(role));
    }
}
