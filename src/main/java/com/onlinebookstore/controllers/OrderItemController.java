package com.onlinebookstore.controllers;

import com.onlinebookstore.commons.exceptions.BookIsNotAvailableException;
import com.onlinebookstore.config.GlobalExceptionHandler;
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

/**
 * Основний метод (шялх) класу - це додавання книги до замовлення ("/order/items/add"). Під час виконання цього методу до загальної ціни замовлення
 * автоматично застосовується знижка на книгу.
 * <p>
 * Також є метод дл яотримання інформації про всі елементи конкретного замовлення ("/order/items/get_all"), що може використовуватись для
 * відображення коризни покупок. Метод за адресою "/order/items/remove" має видаляти елемент замовлення із корзини.
 */

@RestController
@RequestMapping("/order/items")
public class OrderItemController {
    @Autowired
    private OrderItemsService orderItemsService;
    private OrdersService ordersService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;


    // USER, ADMIN
    @GetMapping("/get_all")
    public ResponseEntity<List<OrderItemDTO>> getOrderItems(@RequestParam("order") Integer orderID) {
        List<OrderItemDTO> orderItems = orderItemsService.getOrderItemsDTOByOrderId(orderID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(orderItems, headers, HttpStatus.OK);
    }


    /**
     * Adding a book to the user's cart (order) is implemented here. First, there is a check to see if there are any unconfirmed orders,
     * if there are, the element is added to it, and if there is no order, it is created and the book is added there.
     */
    // USER, ADMIN
    @PostMapping("/add")
    public ResponseEntity<OrderItemEntity> addNewOrderItem(@RequestParam("orderItem") OrderItemDTO orderItemDTO, @RequestParam("user") Integer userId) {
        OrderEntity userOrder;
        try {
            userOrder = ordersService.findWaitingOrderOfUser(userId);
        } catch (EntityNotFoundException e) {
            userOrder = ordersService.addOrder(userId);
        }

        OrderItemEntity orderItem = null;
        try {
            orderItem = orderItemsService.addOrderItemToOrder(orderItemDTO, userOrder.getId());
        } catch (BookIsNotAvailableException e) {
            globalExceptionHandler.bookIsNotAvailableException(e);
        } catch (IllegalArgumentException f) {
            globalExceptionHandler.illegalArgumentWhenBuying(f);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderItem);
    }

    // USER, ADMIN
    @PostMapping("/remove")
    public ResponseEntity.BodyBuilder removeOrderItem(@PathVariable OrderItemEntity orderItem) {
        orderItemsService.deleteOrderItem(orderItem);
        return ResponseEntity.ok();
    }

}
