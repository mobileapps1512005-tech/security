package com.jaiswal.security.SecurityConfig;

import com.jaiswal.security.jwtReposority.JwtAuthFilter;
import com.jaiswal.security.userCustomDetails.UserCustomDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private UserCustomDetails customDetails;

        @Autowired
        private JwtAuthFilter jwtAuthFilter;

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authProvider() {

            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(customDetails);
            provider.setPasswordEncoder(passwordEncoder());

            return provider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/login", "/register").permitAll()
                            .requestMatchers("/admin").hasAuthority("admin")
                            .requestMatchers("/user").hasAuthority("user")
                            .requestMatchers("/owner").hasAuthority("owner")
                            .anyRequest().authenticated())
                            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .authenticationProvider(authProvider())
                    .httpBasic(Customizer.withDefaults());

            return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }



}
