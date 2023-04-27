package com.onlinebookstore.services;

import com.onlinebookstore.domain.OrderItemEntity;
import com.onlinebookstore.models.OrderItemDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface OrderItemsService {

    void addOrderItemToOrder(OrderItemDTO orderItemDTO, Integer orderId);

    void updateOrderPrice(Integer orderID, Integer orderItemID);

    List<OrderItemEntity> getOrderItemsByOrderId(Integer orderID);
    OrderItemEntity getOrderItemById(Integer itemID);

    void deleteOrderItemByID(Integer orderItemID);
    void deleteOrderItem(OrderItemEntity orderItem);
}
