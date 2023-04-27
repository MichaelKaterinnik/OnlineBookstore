package com.onlinebookstore.models;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class OrderItemDTO {
    private Integer id;
    private Integer orderId;
    private Integer bookId;
    private Integer quantity;
    private BigDecimal bookPrice;


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
        this.bookPrice = bookPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDTO that = (OrderItemDTO) o;
        return id.equals(that.id) && Objects.equals(orderId, that.orderId) && Objects.equals(bookId, that.bookId) && Objects.equals(quantity, that.quantity) && Objects.equals(bookPrice, that.bookPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, bookId, quantity, bookPrice);
    }
}
