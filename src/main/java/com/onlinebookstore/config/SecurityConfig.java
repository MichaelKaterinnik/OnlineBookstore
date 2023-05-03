package com.onlinebookstore.config;

import com.onlinebookstore.security.CustomUserDetailsService;
import com.onlinebookstore.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Реалізація управління Security Chain поведінкою додатку. Фільтрація запитів, які доступні всім, а які - лише
 * зареєстрованим користувачам або адміністраторам додатку.
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
           httpSecurity.authorizeHttpRequests()
                .requestMatchers("/authors/add", "/authors/delete", "/authors/delete/{id}", "/authors/update/**",
                        "/books/add", "/books/delete", "/books/delete/{id}", "/books/update", "/books/update/**",
                        "/collection/add", "/collection/delete", "/collection/delete/{id}", "/collection/update/**", "/collection/books/**",
                        "/order/delete/{id}",
                        "/discounts/**",
                        "/user/get_users")
                    .hasAuthority("ADMIN")
                .requestMatchers("/order/add", "/order/user_order", "/order/user_history", "/order/confirm", "/order/items/**",
                        "/user/cabinet", "/user_wishlist/**",
                        "/reviews/add", "/reviews/update/{id}", "/reviews/delete/{id}")
                    .hasAnyAuthority("USER", "ADMIN")
                .anyRequest()
                    .permitAll()

                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
