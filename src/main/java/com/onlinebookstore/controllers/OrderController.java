package com.onlinebookstore.controllers;

import com.onlinebookstore.commons.exceptions.DiscountCannotBeAppliedToThisOrder;
import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.services.OrderItemsService;
import com.onlinebookstore.services.OrdersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderItemsService orderItemsService;


    // GUEST, USER, ADMIN
    @PostMapping("/add")
    public ResponseEntity<OrderEntity> createUserOrder(@RequestBody Integer userID) {
        OrderEntity createdOrder = ordersService.addOrder(userID);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // GUEST, USER, ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Integer orderID) {
        ordersService.deleteOrderById(orderID);
        return ResponseEntity.ok().body("Замовлення №" + orderID + " було успішно видалене.");
    }

    // GUEST, USER, ADMIN
    @GetMapping("/user_order")
    public ResponseEntity<OrderEntity> getUserBasket(@RequestParam("user") UserEntity user) {
        OrderEntity unconfirmedUserOrder = null;
        List<OrderEntity> userOrdersHistory = ordersService.getUserOrders(user);
        for (OrderEntity order : userOrdersHistory) {
            if (order.getStatus() == OrderEntity.OrderStatus.WAITING)
                unconfirmedUserOrder = order;
            break;
        }

        if (unconfirmedUserOrder == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(unconfirmedUserOrder, headers, HttpStatus.OK);
    }

    // USER, ADMIN
    @GetMapping("/user_history")
    public ResponseEntity<List<OrderEntity>> getUserOrdersPageable(@RequestParam("user") UserEntity user,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<OrderEntity> userOrdersHistory = ordersService.getUserOrdersPageable(user, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(userOrdersHistory, headers, HttpStatus.OK);
    }


    @PutMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmOrder(@RequestParam("order") OrderEntity order, @RequestParam("user") UserEntity user, @RequestParam("promo") String promoCode) {
        try {
            ordersService.applyUserDiscountToOrder(order, user.getId());
        } catch (DiscountCannotBeAppliedToThisOrder e) {
            throw new RuntimeException(e);
        }

        try {
            ordersService.applyPromoCode(order, promoCode);
        } catch (EntityNotFoundException e) {
            // throw new RuntimeException(e);
        } catch (DiscountCannotBeAppliedToThisOrder ignored) {

        }

        // payment
        PaymentResponse response = new PaymentResponse(order.getTotalPrice());

        ordersService.updateOrderStatus(order.getId(), OrderEntity.OrderStatus.PROCESSING);
        return ResponseEntity.ok().body(response);
    }



    public static class PaymentResponse {
        private BigDecimal totalAmount;

        public PaymentResponse(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }
    }
}