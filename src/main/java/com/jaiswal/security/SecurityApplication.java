package com.jaiswal.security;

import com.jaiswal.security.userServices.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@Component
@SpringBootApplication
public class SecurityApplication implements CommandLineRunner {


    private final UserServices userServices;

    public SecurityApplication(UserServices userServices){
        this.userServices=userServices;
    }

   public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        userServices.CreateAdmin();
    }
}


