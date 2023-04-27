package com.onlinebookstore.services;

import com.onlinebookstore.domain.OrderDiscountEntity;
import com.onlinebookstore.domain.OrderEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface OrderDiscountsService {

    OrderDiscountEntity findById(Integer id);
    OrderDiscountEntity findByOrderId(Integer id);
    OrderDiscountEntity findByOrder(OrderEntity order);

    void updateDiscountIdForOrderDiscount(Integer id, Integer discountId);

    void delete(OrderDiscountEntity entity);
    void deleteById(Integer id);
}
