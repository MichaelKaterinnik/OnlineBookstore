package com.onlinebookstore.services;

import com.onlinebookstore.domain.OrderItemEntity;
import com.onlinebookstore.models.OrderItemDTO;

import java.util.List;

public interface OrderItemsService {

    OrderItemEntity addOrderItemToOrder(OrderItemDTO orderItemDTO, Integer orderId);

    void updateOrderPrice(Integer orderID, OrderItemEntity orderItem);

    List<OrderItemEntity> getOrderItemsByOrderId(Integer orderID);
    OrderItemEntity getOrderItemById(Integer itemID);

    void deleteOrderItemByID(Integer orderItemID);
    void deleteOrderItem(OrderItemEntity orderItem);
}
