package com.myproject.mybackend.jwt;

import com.myproject.mybackend.user.model.dto.UserDTO;
import com.myproject.mybackend.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        System.out.println("userId = " + userId);

        UserDTO user = userService.findUserById(userId);

        if (user == null) {
            return null;
        }

        return new AuthDetails(user);
    }
}
