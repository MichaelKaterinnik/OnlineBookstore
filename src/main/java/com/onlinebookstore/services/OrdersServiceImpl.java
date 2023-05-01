package com.onlinebookstore.services;

import com.onlinebookstore.commons.exceptions.DiscountCannotBeAppliedToThisOrder;
import com.onlinebookstore.dao.OrderDao;
import com.onlinebookstore.domain.*;
import com.onlinebookstore.models.OrderDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.onlinebookstore.domain.OrderEntity.OrderStatus.WAITING;

@Component
@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrderDao orderRepository;
    @Autowired
    private BooksService booksService;
    @Autowired
    private DiscountsService discountsService;
    @Autowired
    private UserDiscountsService userDiscountsService;


    public OrderEntity createOrder() {
        return new OrderEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addOrder(OrderDTO orderDTO) {
        OrderEntity newOrder = createOrder();
        newOrder.setUserId(orderDTO.getUserId());
        newOrder.setStatus(WAITING);
        orderRepository.save(newOrder);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderEntity addOrder(Integer userID) {
        OrderEntity newOrder = createOrder();
        newOrder.setUserId(userID);
        newOrder.setStatus(WAITING);
        orderRepository.save(newOrder);
        return newOrder;
    }


    // get-methods:
    public OrderEntity getOrderById(Integer orderID) throws EntityNotFoundException {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderID);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else throw new EntityNotFoundException();
    }
    public List<OrderEntity> getUserOrderHistoryById(Integer userId) {
        return orderRepository.findAllByUserId(userId);
    }
    public List<OrderEntity> getUserOrdersPageable(UserEntity user, Pageable pageable) {
        return orderRepository.findAllByUser(user);
    }
    public List<OrderEntity> getUserOrders(UserEntity user) {
        return orderRepository.findAllByUser(user);
    }
    public List<BookEntity> getOrderBooksByOrderId(Integer orderID) {
        return booksService.findBooksByOrderId(orderID);
    }


    // update-methods:
    /**
     * WAITING - until the order is sent to the server (it is possible to change the composition of the order and the price)
     * PROCESSING - after sending the order to the store, the composition and price cannot be changed
     * DEPARTED - issued and sent to the customer by mail
     * COMPLETED - done
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateOrderStatus(Integer orderID, OrderEntity.OrderStatus status) {
        orderRepository.updateOrderStatus(orderID, status);
    }

    /**
     * The method of claiming a discount using an already registered user Discount. It is applied after adding all the books to the order, before
     * applying the discount according to the promotional code and sending the order (by changing the status to 'PROCESSING')
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void applyUserDiscountToOrder(OrderEntity order, Integer userID) throws DiscountCannotBeAppliedToThisOrder {
        UserDiscountEntity userDiscount = userDiscountsService.findUserDiscountByUserId(userID);
        if (userDiscount != null) {
            DiscountEntity discount = discountsService.findDiscountById(userDiscount.getDiscountId());

            BigDecimal discountPercentage = discount.getDiscountPercentage();
            if (order.getStatus().equals(WAITING)) {
                BigDecimal oldPrice = order.getTotalPrice();
                BigDecimal discountedPrice = oldPrice.multiply(BigDecimal.valueOf(100).subtract(discountPercentage)).divide(BigDecimal.valueOf(100));

                order.setTotalPrice(discountedPrice);
            } else {
                throw new DiscountCannotBeAppliedToThisOrder();
            }
        }
    }

    /**
     * The method of claiming a discount using a promotional code. Applied last when ordering (after book discounts and custom discount)
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void applyPromoCode(OrderEntity order, String promoCode) throws EntityNotFoundException, DiscountCannotBeAppliedToThisOrder {
        DiscountEntity orderDiscount = null;
        try {
            orderDiscount = discountsService.findDiscountByCode(promoCode);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e);
        }
        BigDecimal discountPercentage = orderDiscount.getDiscountPercentage();
        if (order.getStatus().equals(WAITING)) {
            BigDecimal oldOrderPrice = order.getTotalPrice();
            BigDecimal discountedPrice = oldOrderPrice.multiply(BigDecimal.valueOf(100).subtract(discountPercentage)).divide(BigDecimal.valueOf(100));
            order.setTotalPrice(discountedPrice);
        } else {
            throw new DiscountCannotBeAppliedToThisOrder();
        }
    }


    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteOrder(OrderEntity order) {
        orderRepository.delete(order);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteOrderById(Integer orderID) {
        orderRepository.deleteById(orderID);
    }
}
