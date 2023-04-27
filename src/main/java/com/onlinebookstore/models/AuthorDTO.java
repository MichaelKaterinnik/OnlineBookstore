package com.onlinebookstore.models;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthorDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String bio;

    public Integer getId() {
        return id;
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
        AuthorDTO authorDTO = (AuthorDTO) o;
        return id.equals(authorDTO.id) && firstName.equals(authorDTO.firstName) && lastName.equals(authorDTO.lastName) && Objects.equals(bio, authorDTO.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, bio);
    }
}
