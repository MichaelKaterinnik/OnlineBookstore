package com.onlinebookstore.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "collection_book", schema = "online_bookstore")
public class CollectionBookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "collection_id", insertable = false, updatable = false)
    private Integer collectionId;
    @Basic
    @Column(name = "book_id", insertable = false, updatable = false)
    private Integer bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", referencedColumnName = "id")
    private GenreCollectionEntity collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
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
        CollectionBookEntity that = (CollectionBookEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(collectionId, that.collectionId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, collectionId, bookId);
    }
}
