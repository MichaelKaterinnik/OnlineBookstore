package com.onlinebookstore.domain;

import jakarta.persistence.*;

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
    private String title;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "author_id", insertable = false, updatable = false)
    private Integer authorId;
    @Basic
    @Column(name = "collection_id", insertable = false, updatable = false)
    private Integer collectionId;
    @Basic
    @Column(name = "rating")
    private BigDecimal rating;
    @Basic
    @Column(name = "price")
    private BigDecimal price;
    @Basic
    @Column(name = "quantity")
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
