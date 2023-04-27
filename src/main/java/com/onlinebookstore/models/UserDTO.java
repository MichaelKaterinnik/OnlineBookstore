package com.onlinebookstore.models;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Objects;

@Component
public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private Timestamp createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id.equals(userDTO.id) && firstName.equals(userDTO.firstName) && Objects.equals(lastName, userDTO.lastName) && phone.equals(userDTO.phone) && Objects.equals(email, userDTO.email) && password.equals(userDTO.password) && Objects.equals(createTime, userDTO.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, email, password, createTime);
    }
}
