package com.jaiswal.security.userServices;

import com.jaiswal.security.DTO.RegisterModel;
import com.jaiswal.security.UserEntity.Users;

import java.util.Optional;


public interface  UserServices {

    // register
    Users register(RegisterModel registerModel);
     // check use and loginByEmail
    Users findByEmail(String email);

    void CreateAdmin();



}
