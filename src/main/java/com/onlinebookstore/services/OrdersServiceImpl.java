package com.onlinebookstore.services;

import com.onlinebookstore.commons.exceptions.DiscountCannotBeAppliedToThisOrder;
import com.onlinebookstore.dao.OrderDao;
import com.onlinebookstore.domain.*;
import com.onlinebookstore.models.OrderDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.onlinebookstore.domain.OrderEntity.OrderStatus.WAITING;

public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrderDao orderRepository;
    @Autowired
    private BooksService booksService;
    @Autowired
    private DiscountsService discountsService;
    @Autowired
    private UserDiscountsServiceImpl userDiscountsService;


    public OrderEntity createOrder() {
        return new OrderEntity();
    }


    public void addOrder(OrderDTO orderDTO) {
        OrderEntity newOrder = createOrder();
        newOrder.setUserId(orderDTO.getUserId());
        newOrder.setStatus(WAITING);
        orderRepository.save(newOrder);
    }


    public OrderEntity getOrderById(Integer orderID) throws EntityNotFoundException {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderID);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else throw new EntityNotFoundException();
    }
    public List<OrderEntity> getUserOrderHistoryById(Integer userId) {
        return orderRepository.findAllByUserId(userId);
    }
    public List<OrderEntity> getUserOrders(UserEntity user) {
        return orderRepository.findAllByUser(user);
    }
    public List<BookEntity> getOrderBooksByOrderId(Integer orderID) {
        return booksService.findBooksByOrderId(orderID);
    }

    /**
     * WAITING - до відправки замовлення на сервер (можливо змінювати склад замовлення та ціну)
     * PROCESSING - після відправки замовлення до магазину, не може змінюватись склад та ціна
     * DEPARTED - оформлене та відправлене до замовника поштою
     * COMPLETED - виконане
     */
    public void updateOrderStatus(Integer orderID, OrderEntity.OrderStatus status) {
        orderRepository.updateOrderStatus(orderID, status);
    }

    /**
     * Метод застоування знижки за промокодом/ Застосовується після додавання всіх книг до замовлення, перед
     * застосуванням знижки за промокодом та відправленням замовлення (зміною на статус 'PROCESSING')
     */
    public void applyUserDiscountToOrder(OrderEntity order, Integer userID) throws DiscountCannotBeAppliedToThisOrder {
        UserDiscountEntity userDiscount = userDiscountsService.findUserDiscountByUserId(userID);
        DiscountEntity discount = discountsService.findDiscountById(userDiscount.getDiscountId());

        BigDecimal discountPercentage = discount.getDiscountPercentage();
        if (order.getStatus().equals(WAITING)) {
            BigDecimal oldPrice = order.getTotalPrice();
            BigDecimal discountedPrice = oldPrice.multiply(BigDecimal.valueOf(100).subtract(discountPercentage)).divide(BigDecimal.valueOf(100));

            order.setTotalPrice(discountedPrice);
        } else {
            throw new DiscountCannotBeAppliedToThisOrder();        }
    }

    /**
     * Метод застоування знижки за промокодом/ Застосовується останнім при формування замовлення (після знижок
     * на книги та користувацької знижки
     */
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

    public void deleteOrder(OrderEntity order) {
        orderRepository.delete(order);
    }
    public void deleteOrderById(Integer orderID) {
        orderRepository.deleteById(orderID);
    }
}
