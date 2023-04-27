package com.onlinebookstore.services;

import com.onlinebookstore.dao.OrderDiscountDao;
import com.onlinebookstore.domain.OrderDiscountEntity;
import com.onlinebookstore.domain.OrderEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class OrderDiscountsServiceImpl implements OrderDiscountsService {
    @Autowired
    private OrderDiscountDao orderDiscountRepository;


    public OrderDiscountEntity createOrderDiscount() {
        return new OrderDiscountEntity();
    }


    public OrderDiscountEntity findById(Integer id) throws EntityNotFoundException {
        Optional<OrderDiscountEntity> optionalOrderDiscount = orderDiscountRepository.findById(id);
        if (optionalOrderDiscount.isPresent()) {
            return optionalOrderDiscount.get();
        } else throw new EntityNotFoundException();
    }
    public OrderDiscountEntity findByOrderId(Integer id) throws EntityNotFoundException  {
        Optional<OrderDiscountEntity> optionalOrderDiscount = orderDiscountRepository.findByOrderId(id);
        if (optionalOrderDiscount.isPresent()) {
            return optionalOrderDiscount.get();
        } else throw new EntityNotFoundException();
    }
    public OrderDiscountEntity findByOrder(OrderEntity order) {
        Optional<OrderDiscountEntity> optionalOrderDiscount = orderDiscountRepository.findByOrder(order);
        if (optionalOrderDiscount.isPresent()) {
            return optionalOrderDiscount.get();
        } else throw new EntityNotFoundException();
    }

    public void updateDiscountIdForOrderDiscount(Integer id, Integer discountId) {
        orderDiscountRepository.updateDiscountId(id, discountId);
    }

    public void delete(OrderDiscountEntity entity) {
        orderDiscountRepository.delete(entity);
    }
    public void deleteById(Integer id) {
        orderDiscountRepository.deleteById(id);
    }
}
