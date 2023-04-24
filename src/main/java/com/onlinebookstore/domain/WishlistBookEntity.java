package com.onlinebookstore.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "wishlist_book", schema = "online_bookstore")
public class WishlistBookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "wishlist_id", insertable = false, updatable = false)
    private Integer wishlistId;
    @Basic
    @Column(name = "book_id", insertable = false, updatable = false)
    private Integer bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", referencedColumnName = "id")
    private WishlistEntity wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Integer wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistBookEntity that = (WishlistBookEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(wishlistId, that.wishlistId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, wishlistId, bookId);
    }
}
