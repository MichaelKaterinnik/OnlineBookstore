package com.onlinebookstore.controllers;

import com.onlinebookstore.config.GlobalExceptionHandler;
import com.onlinebookstore.models.AuthentificationRequest;
import com.onlinebookstore.models.AuthentificationResponse;
import com.onlinebookstore.models.RegisterRequest;
import com.onlinebookstore.security.AuthentificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class JwtController {
    @Autowired
    private AuthentificationService authentificationService;
    private GlobalExceptionHandler exceptionHandler;

    @PostMapping("/register")
    public AuthentificationResponse register(@RequestBody RegisterRequest registerRequest) {
        return authentificationService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthentificationResponse> login(@RequestBody AuthentificationRequest authentificationRequest) {
        try {
            return ResponseEntity.ok().body(authentificationService.login(authentificationRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthentificationResponse("Помилка входу у аккаунт: " + e.getMessage()));
        }
    }
}
