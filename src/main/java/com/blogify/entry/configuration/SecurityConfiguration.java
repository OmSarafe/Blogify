package com.blogify.entry.configuration;

import ch.qos.logback.core.encoder.Encoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.authorizeHttpRequests(auth->auth.anyRequest()
                .authenticated());

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
