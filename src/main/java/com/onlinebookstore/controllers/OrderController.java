package com.onlinebookstore.controllers;

import com.onlinebookstore.config.GlobalExceptionHandler;
import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.OrderDTO;
import com.onlinebookstore.services.OrdersService;
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
    private GlobalExceptionHandler exceptionHandler;



    // USER, ADMIN
    @GetMapping("/user_order")
    public ResponseEntity<OrderEntity> getUserBasket(@RequestParam("user") UserEntity user) {
        OrderEntity unconfirmedUserOrder = ordersService.findWaitingOrderOfUser(user.getId());
        if (unconfirmedUserOrder == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(unconfirmedUserOrder, headers, HttpStatus.OK);
    }

    // USER, ADMIN
    @PutMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmOrder(@RequestParam("order") OrderEntity order,
                                                        @RequestParam("user") UserEntity user,
                                                        @RequestParam("promo") String promoCode) {
        // finding user discount to apply to order price
        ordersService.applyUserDiscountToOrder(order, user.getId());

        // finding order discount (promocode) to apply to order price
        ordersService.applyPromoCode(order, promoCode);

        // payment
        PaymentResponse response = new PaymentResponse(order.getTotalPrice());

        // confirmation
        ordersService.updateOrderStatus(order.getId(), OrderEntity.OrderStatus.PROCESSING);
        return ResponseEntity.ok().body(response);
    }

    // USER, ADMIN
    @GetMapping("/user_history")
    public ResponseEntity<List<OrderDTO>> getUserOrdersPageable(@RequestParam("user") UserEntity user,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<OrderDTO> userOrdersHistory = ordersService.getUserOrdersDTOPageable(user, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(userOrdersHistory, headers, HttpStatus.OK);
    }



    // ADMIN
    @PostMapping("/add")
    public ResponseEntity<OrderEntity> createUserOrder(@RequestBody Integer userID) {
        OrderEntity createdOrder = ordersService.addOrder(userID);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity.BodyBuilder deleteOrder(@PathVariable Integer orderID) {
        ordersService.deleteOrderById(orderID);
        return ResponseEntity.ok();
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
