package com.jaiswal.security.userRepository;

import com.jaiswal.security.DTO.LoginRequest;
import com.jaiswal.security.UserEntity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {
    // check already exists email
    boolean existsByEmail(String email);
    // check already exists phone number
    boolean existsByPhone(String phone);
    // fetch user by email for authentication login
    Optional<Users> findByEmail(String email);


}
