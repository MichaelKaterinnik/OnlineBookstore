package com.onlinebookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity
@Table(name = "categories", schema = "online_bookstore")
public class CategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    @Pattern(regexp = "^[а-щА-ЩЬьЮюЯяІіЇїЄєҐґ'\\s]+$", message = "Назва категорії книг повинна бути українською мовою")
    private String name;
    @Basic
    @Column(name = "description")
    @Pattern(regexp = "^[0-9а-щА-ЩЬьЮюЯяІіЇїЄєҐґ\\p{Punct}\\s]*$", message = "Опис категорії книг повинен бути українською мовою")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
