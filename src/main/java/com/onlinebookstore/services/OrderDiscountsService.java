package com.onlinebookstore.services;

import com.onlinebookstore.domain.OrderDiscountEntity;
import com.onlinebookstore.domain.OrderEntity;

public interface OrderDiscountsService {

    OrderDiscountEntity findById(Integer id);
    OrderDiscountEntity findByOrderId(Integer id);
    OrderDiscountEntity findByOrder(OrderEntity order);

    void updateDiscountIdForOrderDiscount(Integer id, Integer discountId);

    void delete(OrderDiscountEntity entity);
    void deleteById(Integer id);
}
