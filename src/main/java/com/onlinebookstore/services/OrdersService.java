package com.onlinebookstore.services;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.OrderDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersService {

    void addOrder(OrderDTO orderDTO);
    OrderEntity addOrder(Integer userID);

    OrderEntity getOrderById(Integer orderID);
    OrderEntity findWaitingOrderOfUser(Integer userID);
    List<OrderEntity> getUserOrderHistoryById(Integer userId);
    List<OrderEntity> getUserOrdersPageable(UserEntity user, Pageable pageable);
    List<OrderDTO> getUserOrdersDTOPageable(UserEntity user, Pageable pageable);

    List<OrderEntity> getUserOrders(UserEntity user);
    List<BookEntity> getOrderBooksByOrderId(Integer orderID);

    void updateOrderStatus(Integer orderID, OrderEntity.OrderStatus status);

    void applyUserDiscountToOrder(OrderEntity order, Integer userID);
    void applyPromoCode(OrderEntity order, String promoCode);

    void deleteOrder(OrderEntity order);
    void deleteOrderById(Integer orderID);
}
