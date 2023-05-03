package com.onlinebookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.domain.WishlistBookEntity;
import com.onlinebookstore.domain.WishlistEntity;
import com.onlinebookstore.services.BooksService;
import com.onlinebookstore.services.UsersService;
import com.onlinebookstore.services.WishlistsService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WishlistControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UsersService usersService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private WishlistsService wishlistsService;
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before("")
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void testAddBookToWishlist() throws Exception {
        Integer userId = 1;
        Integer bookId = 2;
        UserEntity user = new UserEntity();
        user.setId(userId);
        BookEntity book = new BookEntity();
        book.setId(bookId);
        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setUserId(userId);
        wishlist.setId(1);
        WishlistBookEntity expectedWishlistBook = new WishlistBookEntity();
        expectedWishlistBook.setWishlistId(1);
        expectedWishlistBook.setBookId(bookId);

        when(usersService.findById(userId)).thenReturn(user);
        when(booksService.findBookByID(bookId)).thenReturn(book);
        when(wishlistsService.findWishlistByUserId(userId)).thenReturn(wishlist);
        when(wishlistsService.addBookToWishlist(1, bookId)).thenReturn(expectedWishlistBook);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user_wishlist/add_book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userId) + objectMapper.writeValueAsString(bookId)))
                .andExpect(status().isCreated())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        WishlistBookEntity actualWishlistBook = objectMapper.readValue(response.getContentAsString(), WishlistBookEntity.class);
        assertNotNull(actualWishlistBook);
        assertEquals(expectedWishlistBook.getWishlistId(), actualWishlistBook.getWishlistId());
        assertEquals(expectedWishlistBook.getBookId(), actualWishlistBook.getBookId());
    }




}
