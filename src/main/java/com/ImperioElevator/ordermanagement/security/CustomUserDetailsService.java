package com.ImperioElevator.ordermanagement.security;

import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserDaoImpl userDao;

    public CustomUserDetailsService(UserDaoImpl userDao) {
        this.userDao = userDao;
    }


    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = null;
        try {
            user = userDao.findByUserEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found 'email'");
        }

        return new org.springframework.security.core.userdetails.User(
                user.email().email(),
                user.password(),
                user.roles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList())
        );
    }
}
