package com.onlinebookstore.models;

import com.onlinebookstore.domain.OrderEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Component
public class OrderDTO {
    private Integer id;
    private Integer userId;
    private Timestamp createdAt;
    private OrderEntity.OrderStatus status;
    private BigDecimal totalPrice;


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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public OrderEntity.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderEntity.OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id.equals(orderDTO.id) && Objects.equals(userId, orderDTO.userId) && createdAt.equals(orderDTO.createdAt) && status == orderDTO.status && Objects.equals(totalPrice, orderDTO.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, createdAt, status, totalPrice);
    }
}
