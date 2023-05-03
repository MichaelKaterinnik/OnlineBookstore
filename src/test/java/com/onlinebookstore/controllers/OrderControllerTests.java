package com.onlinebookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.domain.*;
import com.onlinebookstore.models.OrderDTO;
import com.onlinebookstore.models.OrderItemDTO;
import com.onlinebookstore.services.*;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class OrderControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UsersService usersService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private WishlistsService wishlistsService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private BookDiscountsService bookDiscountsService;
    @Autowired
    private DiscountsService discountsService;
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private UserDiscountsService userDiscountsService;

    private MockMvc mockMvc;

    private ModelMapper modelMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before("")
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void testGetUserOrdersPageable() throws Exception {
        // prepare data
        UserEntity user = new UserEntity();
        user.setId(1);
        OrderEntity order1 = new OrderEntity();
        order1.setUserId(user.getId());
        order1.setTotalPrice(BigDecimal.valueOf(50));
        OrderEntity order2 = new OrderEntity();
        order2.setUserId(user.getId());
        order2.setTotalPrice(BigDecimal.valueOf(100));
        List<OrderEntity> userOrders = Arrays.asList(order1, order2);

        // set up mock services
        when(ordersService.getUserOrdersDTOPageable(user, PageRequest.of(0, 20))).thenReturn(
                        (List<OrderDTO>) userOrders.stream().map(order -> modelMapper.map(order, OrderDTO.class)));

        // make request to controller
        mockMvc.perform(MockMvcRequestBuilders.get("/order/user_history")
                        .param("user", String.valueOf(user.getId()))
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$[0].id", is(order1.getId())))
                .andExpect((ResultMatcher) jsonPath("$[0].totalPrice", is(order1.getTotalPrice())))
                .andExpect((ResultMatcher) jsonPath("$[1].id", is(order2.getId())))
                .andExpect((ResultMatcher) jsonPath("$[1].totalPrice", is(order2.getTotalPrice())));
    }

    @Test
    void testBookDiscountWhenOrdering() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1);
        BookEntity book = new BookEntity();
        book.setId(1);
        book.setPrice(BigDecimal.valueOf(100));
        book.setQuantity(5);
        book.setAvailability(true);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setBookId(book.getId());
        orderItemDTO.setQuantity(3);

        OrderItemEntity returned = null;

        when(booksService.findBookByID(book.getId())).thenReturn(book);
        when(bookDiscountsService.ifBookIsDiscounted(book.getId())).thenReturn(true);
        when(bookDiscountsService.findBookDiscountByBookId(book.getId())).thenReturn(new BookDiscountEntity());
        when(discountsService.findDiscountById(anyInt())).thenReturn(new DiscountEntity());
        when(ordersService.findWaitingOrderOfUser(user.getId())).thenReturn(new OrderEntity());
        when(orderItemsService.addOrderItemToOrder(eq(orderItemDTO), anyInt())).thenReturn(returned = new OrderItemEntity());

        // make request to controller
        mockMvc.perform(post("/order/items/add")
                        .param("orderItem", objectMapper.writeValueAsString(orderItemDTO))
                        .param("user", String.valueOf(user.getId())))
                .andExpect(status().isCreated());

        // check if book price was discounted
        ArgumentCaptor<OrderItemEntity> captor = ArgumentCaptor.forClass(OrderItemEntity.class);
        verify(bookDiscountsService, times(1)).applyBookDiscountWhenOrdering(returned, eq(book));
        assertEquals(BigDecimal.valueOf(80), captor.getValue().getBookPrice());

        // check if order item price was calculated correctly
        BigDecimal expectedPrice = BigDecimal.valueOf(80);
        verify(orderItemsService, times(1)).addOrderItemToOrder(orderItemDTO, anyInt());
        verify(orderItemsService, times(1)).updateOrderPrice(anyInt(), eq(new OrderItemEntity()));
    }

    @Test
    void testApplyUserDiscountToOrder() {
        UserEntity user = new UserEntity();
        user.setId(1);

        OrderEntity order = new OrderEntity();
        order.setId(1);
        order.setUserId(user.getId());

        UserDiscountEntity userDiscount = new UserDiscountEntity();
        userDiscount.setId(1);
        userDiscount.setUserId(user.getId());
        userDiscount.setDiscountId(1);

        DiscountEntity discount = new DiscountEntity();
        discount.setId(1);
        discount.setDiscountPercentage(BigDecimal.valueOf(20));

        // set up mocks
        when(userDiscountsService.findUserDiscountByUserId(user.getId())).thenReturn(userDiscount);
        when(discountsService.findDiscountById(userDiscount.getDiscountId())).thenReturn(discount);

        ordersService.applyUserDiscountToOrder(order, user.getId());

        BigDecimal expectedPrice = BigDecimal.valueOf(80);
        assertEquals(expectedPrice, order.getTotalPrice());
    }

    @Test
    void testApplyPromoCode() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1);

        BookEntity book = new BookEntity();
        book.setId(1);
        book.setPrice(BigDecimal.valueOf(100));
        book.setQuantity(5);
        book.setAvailability(true);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setBookId(book.getId());
        orderItemDTO.setQuantity(3);

        OrderItemEntity returnedOrderItem = new OrderItemEntity();
        returnedOrderItem.setBookId(book.getId());
        returnedOrderItem.setQuantity(orderItemDTO.getQuantity());
        returnedOrderItem.setBookPrice(book.getPrice());

        OrderEntity waitingOrder = new OrderEntity();
        waitingOrder.setId(1);
        waitingOrder.setUserId(user.getId());
        waitingOrder.setStatus(OrderEntity.OrderStatus.WAITING);
        waitingOrder.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        waitingOrder.setTotalPrice(BigDecimal.valueOf(300));

        // Mock dependencies
        when(booksService.findBookByID(book.getId())).thenReturn(book);
        when(orderItemsService.addOrderItemToOrder(eq(orderItemDTO), eq(waitingOrder.getId()))).thenReturn(returnedOrderItem);
        when(ordersService.findWaitingOrderOfUser(user.getId())).thenReturn(waitingOrder);

        // Apply promo code and check if order price was calculated correctly
        String promoCode = "ABCD1234";
        DiscountEntity promoCodeDiscount = new DiscountEntity();
        promoCodeDiscount.setCode(promoCode);
        promoCodeDiscount.setDiscountPercentage(BigDecimal.valueOf(10));
        when(discountsService.findDiscountByCode(promoCode)).thenReturn(promoCodeDiscount);

        ordersService.applyPromoCode(waitingOrder, promoCode);

        BigDecimal expectedPrice = BigDecimal.valueOf(270); // price with 10% discount
        assertEquals(expectedPrice, waitingOrder.getTotalPrice());
    }


}
