package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.OrderItemEntity;
import com.onlinebookstore.models.OrderItemDTO;
import com.onlinebookstore.services.OrderItemsService;
import com.onlinebookstore.services.OrdersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order/items")
public class OrderItemController {
    @Autowired
    private OrderItemsService orderItemsService;
    private OrdersService ordersService;


    // GUEST, USER, ADMIN
    @GetMapping("/get")
    public ResponseEntity<List<OrderItemEntity>> getOrderItems(@RequestParam("order") Integer orderID) {
        List<OrderItemEntity> orderItems = orderItemsService.getOrderItemsByOrderId(orderID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(orderItems, headers, HttpStatus.OK);
    }

    // GUEST, USER, ADMIN
    @PostMapping("/add")
    public ResponseEntity<OrderItemEntity> addNewOrderItem(@RequestParam("orderItem") OrderItemDTO orderItemDTO, @RequestParam("order") Integer orderId,  @RequestParam("user") Integer userId) {
        OrderEntity userOrder;
        try {
            userOrder = ordersService.getOrderById(orderId);
        } catch (EntityNotFoundException e) {
            userOrder = ordersService.addOrder(userId);
        }
        OrderItemEntity orderItem = orderItemsService.addOrderItemToOrder(orderItemDTO, userOrder.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItem);
    }

    @PostMapping("/remove")
    public ResponseEntity.BodyBuilder removeOrderItem(@PathVariable OrderItemEntity orderItem) {
        orderItemsService.deleteOrderItem(orderItem);
        return ResponseEntity.ok();
    }

}
