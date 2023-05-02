package com.onlinebookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "online_bookstore")
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "first_name")
    @Pattern(regexp = "^[а-щА-ЩЬьЮюЯяІіЇїЄєҐґA-Za-z]{1,45}$", message = "Ім'я користувача повинно бути на українській мові або латиниці, не більше 45 символів")
    private String firstName;
    @Basic
    @Column(name = "last_name")
    @Pattern(regexp = "^[а-щА-ЩЬьЮюЯяІіЇїЄєҐґA-Za-z]{1,128}$", message = "Прізвище користувача повинно бути на українській мові або латиниці, не більше 128 символів")
    private String lastName;
    @Basic
    @Column(name = "phone")
    @Pattern(regexp = "^\\+\\d{12}$", message = "Номер телефону повинен бути у форматі: +38 ___ ___-__-__")
    private String phone;
    @Basic
    @Column(name = "email")
    @Email
    private String email;
    @Basic
    @Column(name = "password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,12}$", message = "Пароль повинен містити від 8 до 12 символів латиницею та мінімум 1 числовий символ")
    private String password;
    @Basic
    @Column(name = "role")
    private Role role;
    @Basic
    @Column(name = "create_time")
    private Timestamp createTime;


    public enum Role {
        USER,
        ADMIN
    }


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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(phone, that.phone) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, email, password, createTime);
    }
}
