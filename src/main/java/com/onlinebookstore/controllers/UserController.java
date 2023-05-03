package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.*;
import com.onlinebookstore.models.BookDTO;
import com.onlinebookstore.models.OrderDTO;
import com.onlinebookstore.models.ReviewDTO;
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
        List<BookDTO> wishlistBooks = wishlistsService.getBooksDTOFromUserWishlist(user.getId());

        OrderDTO unconfirmedUserOrder = null;
        Pageable pageable = PageRequest.of(page, size);
        List<OrderDTO> userOrdersHistory = ordersService.getUserOrdersDTOPageable(user, pageable);
        for (OrderDTO order : userOrdersHistory) {
            if (order.getStatus() == OrderEntity.OrderStatus.WAITING)
                unconfirmedUserOrder = order;
        }

        List<ReviewDTO> userReviews = reviewsService.findUserReviewsDTO(user);

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
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserDTO> users = usersService.getAllUserDTO(pageable);

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
