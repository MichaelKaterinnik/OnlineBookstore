package com.onlinebookstore.services;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.OrderDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface OrdersService {

    void addOrder(OrderDTO orderDTO);

    OrderEntity getOrderById(Integer orderID);
    List<OrderEntity> getUserOrderHistoryById(Integer userId);
    List<OrderEntity> getUserOrders(UserEntity user);
    List<BookEntity> getOrderBooksByOrderId(Integer orderID);

    void updateOrderStatus(Integer orderID, OrderEntity.OrderStatus status);

    void applyUserDiscountToOrder(OrderEntity order, Integer userID);
    void applyPromoCode(OrderEntity order, String promoCode);

    void deleteOrder(OrderEntity order);
    void deleteOrderById(Integer orderID);
}
