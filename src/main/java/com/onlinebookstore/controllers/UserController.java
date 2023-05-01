package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.*;
import com.onlinebookstore.services.OrdersService;
import com.onlinebookstore.services.ReviewsService;
import com.onlinebookstore.services.UsersService;
import com.onlinebookstore.services.WishlistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    // implement after security configure
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtProvider.generateJwtToken(authentication);
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
//    }


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

}
