package com.onlinebookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books", schema = "online_bookstore")
public class BookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "title")
    @Pattern(regexp = "^[\\p{L}0-9\\s,.;:\"'!?()\\[\\]{}«»]+$", message = "Назва книги може містити тільки літери української мови, латинські літери, числа та деякі розділові знаки")
    @Size(max = 2000, message = "Назва повинна містити не більше 255 символів")
    private String title;
    @Basic
    @Column(name = "description")
    @Pattern(regexp = "^[\\p{L}0-9\\s.,;:!?'\"()\\[\\]{}«»-]+$", message = "Опис книги може містити тільки літери української мови, латинські літери, числа та основні розділові знаки")
    @Size(max = 2000, message = "Текст повинен містити не більше 2000 символів")
    private String description;
    @Basic
    @Column(name = "author_id", insertable = false, updatable = false)
    private Integer authorId;
    @Basic
    @Column(name = "collection_id", insertable = false, updatable = false)
    private Integer collectionId;
    @Basic
    @Column(name = "rating")
    @DecimalMin(value = "1.0", message = "Рейтинг книги не може бути меншим за 1.0")
    @DecimalMax(value = "10.0", message = "Рейтинг книги не може бути більшим за 10.0")
    private BigDecimal rating;
    @Basic
    @Column(name = "price")
    @DecimalMin(value = "0.0", message = "Ціна книги не може бути меншою за 0.0")
    @DecimalMax(value = "999999.99", message = "Ціна книги не може бути більшою за 999999.99")
    @Digits(integer = 8, fraction = 2, message = "Ціна книги повинна містити не більше 8 цифр у числовій частині та 2 цифри у дробовій")
    private BigDecimal price;
    @Basic
    @Column(name = "quantity")
    @Min(value = 0, message = "Кількість книг не може бути меншою за 0")
    private Integer quantity;
    @Basic
    @Column(name = "availability")
    private Boolean availability;
    @Basic
    @Column(name = "cover_image")
    private byte[] coverImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private AuthorEntity author;

    @ManyToMany(mappedBy = "books")
    private List<CollectionEntity> collections = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public List<CollectionEntity> getCollections() {
        return collections;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(authorId, that.authorId) && Objects.equals(collectionId, that.collectionId) && Objects.equals(rating, that.rating) && Objects.equals(price, that.price) && Objects.equals(availability, that.availability) && Arrays.equals(coverImage, that.coverImage);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, title, description, authorId, collectionId, rating, price, availability);
        result = 31 * result + Arrays.hashCode(coverImage);
        return result;
    }
}
