package com.onlinebookstore.controller;

import com.onlinebookstore.domain.User;

import com.onlinebookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get_users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> user = userRepository.findAll();

        return ResponseEntity.ok(user);
    }

}
