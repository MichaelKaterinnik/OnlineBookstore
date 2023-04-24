package com.onlinebookstore.controllers;

import com.onlinebookstore.dao.UserDao;
import com.onlinebookstore.domain.UserEntity;
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
    private UserDao userDao;

    @GetMapping("/get_users")
    public ResponseEntity<List<UserEntity>> getUsers() {
        List<UserEntity> user = userDao.findAll();

        return ResponseEntity.ok(user);
    }


}
