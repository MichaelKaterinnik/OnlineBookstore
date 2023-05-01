package com.onlinebookstore.services;

import com.onlinebookstore.dao.OrderDiscountDao;
import com.onlinebookstore.domain.OrderDiscountEntity;
import com.onlinebookstore.domain.OrderEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Service
public class OrderDiscountsServiceImpl implements OrderDiscountsService {
    @Autowired
    private OrderDiscountDao orderDiscountRepository;


    public OrderDiscountEntity createOrderDiscount() {
        return new OrderDiscountEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addNewOrderDiscount(Integer orderID, Integer discountID) {
        OrderDiscountEntity newOrderDiscount = createOrderDiscount();
        newOrderDiscount.setOrderId(orderID);
        newOrderDiscount.setDiscountId(discountID);
        orderDiscountRepository.save(newOrderDiscount);
    }


    // get-methods:
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


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateDiscountIdForOrderDiscount(Integer id, Integer discountId) {
        orderDiscountRepository.updateDiscountId(id, discountId);
    }


    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(OrderDiscountEntity entity) {
        orderDiscountRepository.delete(entity);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        orderDiscountRepository.deleteById(id);
    }
}
