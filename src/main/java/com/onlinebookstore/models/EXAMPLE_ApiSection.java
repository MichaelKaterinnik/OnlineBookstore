package com.onlinebookstore.models;

import java.util.Objects;

/**
 * Клас-модель для опису об'єктів типу - EXAMPLE_ApiSection. Містить id, як ідентифікатор, а також інші поля,
 * які також відображені у таблицях БД.
 */
public class EXAMPLE_ApiSection {
    private Integer id;
    private final String title;
    private final String description;

    public EXAMPLE_ApiSection(Integer id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public EXAMPLE_ApiSection(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EXAMPLE_ApiSection that = (EXAMPLE_ApiSection) o;
        return id == that.id && Objects.equals(title, that.title) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }
}
