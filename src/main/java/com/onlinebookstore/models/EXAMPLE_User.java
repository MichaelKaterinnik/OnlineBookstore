package com.onlinebookstore.models;

import java.util.Objects;

/**
 * Клас-модель для опису об'єктів типу - EXAMPLE_User. Містить id, як ідентифікатор, а також інші поля,
 * які також відображені у таблицях БД.
 */
public class EXAMPLE_User {
    private Integer id;
    private String username;
    private String password;

    public EXAMPLE_User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EXAMPLE_User user = (EXAMPLE_User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
