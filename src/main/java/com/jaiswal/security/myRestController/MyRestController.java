package com.jaiswal.security.myRestController;

import com.jaiswal.security.DTO.*;
import com.jaiswal.security.UserEntity.Users;
import com.jaiswal.security.jwtReposority.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.jaiswal.security.userServices.UserServices;

@RestController
public class MyRestController {

    @Autowired
    private UserServices userServices;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> postRegister(@RequestBody RegisterModel registerModel) {

        // 1. CHECK USER EXISTS (ALWAYS SEPARATE METHOD)
        Users existingUser = userServices.findByEmail(registerModel.getEmail());

        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "User Already Registered", null));
        }

        // 2. ENCODE PASSWORD
        registerModel.setPassword(encoder.encode(registerModel.getPassword()));

        // 3. SAVE USER
        Users savedUser = userServices.register(registerModel);

        if (savedUser != null) {
            String token = jwtService.generateToken(savedUser, jwtService.RegisterExpireTime);

            return ResponseEntity.ok(new ApiResponse(true, "User Registered Successfully", token));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Registration Failed", null));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Users users = userServices.findByEmail(auth.getName());
            String token = jwtService.generateToken(users, jwtService.LoginExpireTime);

            return ResponseEntity.ok(new ApiResponse(true,"Login SuccessFully",token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse(false,"Invalid email or password",null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getProfile(@RequestHeader("Authorization") String token) {

        try {

            // 1. NULL CHECK
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Token missing", null));
            }

            // 2. REMOVE BEARER
            String jwt = token.substring(7);

            // 3. VALIDATE TOKEN
            if (!jwtService.validateToken(jwt)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Token expired or invalid", null));
            }

            // 4. EXTRACT EMAIL
            String email = jwtService.extractEmail(jwt);

            // 5. GET USER
            Users user = userServices.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "User not found", null));
            }

            // 6. RETURN SUCCESS
            return ResponseEntity.ok(new ApiResponse(true, "User profile fetched", user)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Something went wrong", null));
        }
    }

}
