package com.blogify.entry.configuration;

import ch.qos.logback.core.encoder.Encoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails userDetails1 = User.builder()
                .username("om")
                .password(passwordEncoder().encode("om@1234"))
                .build();

        UserDetails userDetails2 = User.builder()
                .username("arati").password(passwordEncoder().
                        encode("arati@1234")).build();

        UserDetails userDetails3 = User.builder()
                .username("yash").password(passwordEncoder().
                        encode("yash@1234")).build();

        return new InMemoryUserDetailsManager(userDetails1,userDetails2,userDetails3);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
