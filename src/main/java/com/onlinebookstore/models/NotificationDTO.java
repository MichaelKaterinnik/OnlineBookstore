package com.onlinebookstore.models;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Objects;

@Component
public class NotificationDTO {
    private Integer id;
    private String message;
    private Integer userId;
    private Integer bookId;
    private Timestamp createdAt;


    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationDTO that = (NotificationDTO) o;
        return id.equals(that.id) && Objects.equals(message, that.message) && userId.equals(that.userId) && Objects.equals(bookId, that.bookId) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, userId, bookId, createdAt);
    }
}
