package com.onlinebookstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.models.ReviewDTO;
import com.onlinebookstore.services.BooksService;
import com.onlinebookstore.services.ReviewsService;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ReviewControllerTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UsersService usersService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private WishlistsService wishlistsService;
    @Autowired
    private ReviewsService reviewsService;
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before("")
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void testCreateReview() throws Exception {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUserId(1);
        reviewDTO.setBookId(2);
        reviewDTO.setComment("Дуже цікава книга!");
        reviewDTO.setRating(BigDecimal.valueOf(8.5));

        ReviewEntity expectedReview = new ReviewEntity();
        expectedReview.setId(1);
        expectedReview.setUserId(1);
        expectedReview.setBookId(2);
        expectedReview.setComment("Дуже цікава книга!");
        expectedReview.setRating(BigDecimal.valueOf(8.5));
        expectedReview.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        expectedReview.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        when(reviewsService.addNewReview(reviewDTO)).thenReturn(expectedReview);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/reviews/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        // checking results...
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        ReviewEntity actualReview = objectMapper.readValue(response.getContentAsString(), ReviewEntity.class);
        assertNotNull(actualReview);
        assertEquals(expectedReview.getId(), actualReview.getId());
        assertEquals(expectedReview.getUserId(), actualReview.getUserId());
        assertEquals(expectedReview.getBookId(), actualReview.getBookId());
        assertEquals(expectedReview.getComment(), actualReview.getComment());
        assertEquals(expectedReview.getRating(), actualReview.getRating());
        assertNotNull(actualReview.getCreatedAt());
        assertNotNull(actualReview.getUpdatedAt());
    }

    @Test
    void testUpdatingReview() throws Exception {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1);
        reviewDTO.setComment("Новий коментар");
        reviewDTO.setRating(BigDecimal.valueOf(9.5));

        mockMvc.perform(MockMvcRequestBuilders.put("/reviews/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("Відгук оновлено!"));

        verify(reviewsService).updateReview(reviewDTO.getId(), reviewDTO.getComment(), reviewDTO.getRating());
    }

    @Test
    void testDeleteReviewById() throws Exception {
        // given
        Integer reviewId = 1;

        // when
        doNothing().when(reviewsService).deleteReviewById(reviewId);
        mockMvc.perform(MockMvcRequestBuilders.delete("/reviews/delete/{id}", reviewId))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("Відгук видалено!"));

        // then
        verify(reviewsService, times(1)).deleteReviewById(reviewId);
    }

}
