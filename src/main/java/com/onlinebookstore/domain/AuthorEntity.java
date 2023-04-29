package com.onlinebookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity
@Table(name = "authors", schema = "online_bookstore")
public class AuthorEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "first_name")
    @Pattern(regexp = "^[а-щА-ЩЬьЮюЯяІіЇїЄєҐґ']{1,30}$", message = "Ім'я автора книги повинне зазначатись українською мовою")
    private String firstName;
    @Basic
    @Column(name = "last_name")
    @Pattern(regexp = "^[а-щА-ЩЬьЮюЯяІіЇїЄєҐґ']{1,45}$", message = "Прізвище автора книги повинне зазначатись українською мовою")
    private String lastName;
    @Basic
    @Column(name = "bio")
    @Pattern(regexp = "^[0-9\\p{Punct}а-щА-ЩЬьЮюЯяІіЇїЄєҐґ\\s]{1,2000}$", message = "Біографія автора повинна зазначатись українською. Допускаються також числові символи та знаки пунктуації")
    private String bio;

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorEntity that = (AuthorEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(bio, that.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, bio);
    }
}
