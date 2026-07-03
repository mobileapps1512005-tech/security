package com.jaiswal.security.userCustomDetails;

import com.jaiswal.security.ExpectionHandel.UsernameNotFoundException;
import com.jaiswal.security.UserEntity.Users;
import com.jaiswal.security.userRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCustomDetails implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> users = userRepository.findByEmail(username);
        if (users.isPresent()) {
           return User.builder()
                    .username(users.get().getEmail())
                    .password(users.get().getPassword())
                    .authorities(users.get().getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException("used not fount : " + username);
        }
    }
}
