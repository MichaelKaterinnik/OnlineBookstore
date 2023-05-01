package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.models.ReviewDTO;
import com.onlinebookstore.services.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewsService reviewsService;


    // GUEST, USER, ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<ReviewEntity> getReviewById(@PathVariable Integer reviewID) {
        ReviewEntity review = reviewsService.findReviewById(reviewID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(review, headers, HttpStatus.OK);
    }

    // GUEST, USER, ADMIN
    @GetMapping("/book_reviews/{id}")
    public ResponseEntity<List<ReviewEntity>> getReviewsOfBook(@PathVariable Integer bookID,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ReviewEntity> bookReviews = reviewsService.findReviewsByBookId(bookID, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(bookReviews, headers, HttpStatus.OK);
    }

    // GUEST, USER, ADMIN
    @GetMapping("/user_reviews/{id}")
    public ResponseEntity<List<ReviewEntity>> getReviewsOfUser(@PathVariable Integer userID,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ReviewEntity> userReviews = reviewsService.findReviewsByUserID(userID, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(userReviews, headers, HttpStatus.OK);
    }


    // USER, ADMIN
    @PostMapping("/add")
    public ResponseEntity<ReviewEntity> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewEntity createdReview = reviewsService.addNewReview(reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // USER, ADMIN
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateReview(@RequestBody ReviewDTO reviewDTO) {
        reviewsService.updateReview(reviewDTO.getId(), reviewDTO.getComment(), reviewDTO.getRating());
        String response = "Відгук оновлено!";
        return ResponseEntity.ok().body(response);
    }

    // USER, ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteReviewById(@PathVariable Integer rewviewID) {
        reviewsService.deleteReviewById(rewviewID);
        String response = "Відгук видалено!";
        return ResponseEntity.ok().body(response);
    }

}
