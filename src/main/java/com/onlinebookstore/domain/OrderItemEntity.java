package com.onlinebookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items", schema = "online_bookstore")
public class OrderItemEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "order_id", insertable = false, updatable = false)
    private Integer orderId;
    @Basic
    @Column(name = "book_id", insertable = false, updatable = false)
    private Integer bookId;
    @Basic
    @Column(name = "quantity")
    @Min(value = 1, message = "Кількість книг не може бути меншою за 1")
    private Integer quantity;
    @Basic
    @Column(name = "book_price")
    private BigDecimal bookPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity book;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = book.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(orderId, that.orderId) && Objects.equals(bookId, that.bookId) && Objects.equals(quantity, that.quantity) && Objects.equals(bookPrice, that.bookPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, bookId, quantity, bookPrice);
    }
}
