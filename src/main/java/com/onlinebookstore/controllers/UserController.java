package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.*;
import com.onlinebookstore.models.UserDTO;
import com.onlinebookstore.services.OrdersService;
import com.onlinebookstore.services.ReviewsService;
import com.onlinebookstore.services.UsersService;
import com.onlinebookstore.services.WishlistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private WishlistsService wishlistsService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ReviewsService reviewsService;


    // USER, ADMIN
    @GetMapping("/cabinet")
    public Map<String, Object> getUserCabinet(@RequestParam("user") UserEntity user,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        WishlistEntity userWishlist = wishlistsService.findWishlistByUserId(user.getId());
        List<BookEntity> wishlistBooks = wishlistsService.getBooksFromUserWishlist(user.getId());

        OrderEntity unconfirmedUserOrder = null;
        Pageable pageable = PageRequest.of(page, size);
        List<OrderEntity> userOrdersHistory = ordersService.getUserOrdersPageable(user, pageable);
        for (OrderEntity order : userOrdersHistory) {
            if (order.getStatus() == OrderEntity.OrderStatus.WAITING)
                unconfirmedUserOrder = order;
        }

        List<ReviewEntity> userReviews = reviewsService.findUserReviews(user);

        Map<String, Object> response = new HashMap<>();
        response.put("wishlist", userWishlist);
        response.put("wishlistBooks", wishlistBooks);
        response.put("orderHistory", userOrdersHistory);
        response.put("userReviews", userReviews);
        if (unconfirmedUserOrder != null) {
            response.put("unconfirmedOrder", unconfirmedUserOrder);
        }

        return response;
    }


    // ADMIN
    @GetMapping("/get_users")
    public ResponseEntity<List<UserEntity>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserEntity> users = usersService.findAll(pageable);

        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<UserEntity> forceAddingUser(@RequestBody UserDTO userDTO) {
        UserEntity createdUser = usersService.addNewUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Integer userID) {
        usersService.deleteUserById(userID);
        return ResponseEntity.ok().body("Користувач був успішно видалений!");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable UserEntity user) {
        usersService.deleteUser(user);
        return ResponseEntity.ok().body("Користувач був успішно видалений!");
    }

}
