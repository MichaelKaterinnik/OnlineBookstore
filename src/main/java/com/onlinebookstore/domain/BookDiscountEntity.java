package com.onlinebookstore.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "book_discounts", schema = "online_bookstore")
public class BookDiscountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "book_id", insertable=false, updatable=false)
    private Integer bookId;
    @Basic
    @Column(name = "discount_id")
    private Integer discountId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDiscountEntity that = (BookDiscountEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(bookId, that.bookId) && Objects.equals(discountId, that.discountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookId, discountId);
    }
}
