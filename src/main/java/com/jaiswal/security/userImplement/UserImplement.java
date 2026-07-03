package com.jaiswal.security.userImplement;

import com.jaiswal.security.DTO.LoginResponse;
import com.jaiswal.security.DTO.RegisterModel;
import com.jaiswal.security.UserEntity.Users;
import com.jaiswal.security.jwtReposority.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jaiswal.security.userRepository.UserRepository;
import com.jaiswal.security.userServices.UserServices;

import java.util.Optional;

@Service
public class UserImplement implements UserServices {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public Users register(RegisterModel registerModel) {

        boolean existsEmail = userRepository.existsByEmail(registerModel.getEmail());
        boolean existsPhone = userRepository.existsByPhone(registerModel.getPhone());

        if (existsEmail||existsPhone){
         return null;
        }

        Users users = new Users();
        users.setName(registerModel.getName());
        users.setEmail(registerModel.getEmail());
        users.setPassword(registerModel.getPassword());
        users.setPhone(registerModel.getPhone());
        users.setRole(registerModel.getRole());
        users.setStatus(registerModel.getStatus());
       return userRepository.save(users);
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void CreateAdmin() {
        if (userRepository.findByEmail("vasa1515@gmail.com").isPresent()){
            return;
        }
        Users users = new Users();
        users.setName("vasa");
        users.setEmail("vasa1515@gmail.com");
        users.setPassword(passwordEncoder.encode("vasa1515"));
        users.setPhone("9999848479");
        users.setRole("admin");
        users.setStatus("active");
        userRepository.save(users);
        }
    }

