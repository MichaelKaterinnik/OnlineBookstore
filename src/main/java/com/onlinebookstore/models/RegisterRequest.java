package com.onlinebookstore.models;

import com.onlinebookstore.domain.UserEntity;

public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private UserEntity.Role role;

    public RegisterRequest() {
    }

    public RegisterRequest(String firstName, String lastName, String phone, String email, String password, String role) {
        this.email = email;
        this.password = password;
        if (role.equalsIgnoreCase("admin")) {
            this.role = UserEntity.Role.ADMIN;
        } else
            this.role = UserEntity.Role.USER;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public UserEntity.Role getRole() {
        return role;
    }
}
