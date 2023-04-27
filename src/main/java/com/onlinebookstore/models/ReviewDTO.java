package com.onlinebookstore.models;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Component
public class ReviewDTO {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private String comment;
    private BigDecimal rating;
    private Timestamp createdAt;
    private Timestamp updatedAt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDTO reviewDTO = (ReviewDTO) o;
        return id.equals(reviewDTO.id) && Objects.equals(userId, reviewDTO.userId) && Objects.equals(bookId, reviewDTO.bookId) && Objects.equals(comment, reviewDTO.comment) && Objects.equals(rating, reviewDTO.rating) && Objects.equals(createdAt, reviewDTO.createdAt) && Objects.equals(updatedAt, reviewDTO.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, bookId, comment, rating, createdAt, updatedAt);
    }
}
