package com.onlinebookstore.services;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.ReviewDTO;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ReviewsService {

    ReviewEntity addNewReview(ReviewDTO reviewDTO);

    void deleteReviewById(Integer reviewID);
    void deleteReview(ReviewEntity review);

    ReviewEntity findReviewById(Integer reviewID);
    ReviewDTO findReviewDTOById(Integer reviewID);

    List<ReviewEntity> findReviewsByUserID(Integer userID, Pageable pageable);
    List<ReviewDTO> findReviewsDTOByUserID(Integer userID, Pageable pageable);

    List<ReviewEntity> findUserReviews(UserEntity user);
    List<ReviewDTO> findUserReviewsDTO(UserEntity user);

    List<ReviewEntity> findReviewsByBookId(Integer bookID, Pageable pageable);
    List<ReviewDTO> findReviewsDTOByBookId(Integer bookID, Pageable pageable);

    List<ReviewEntity> findBookReviews(BookEntity book);

    void updateReviewComment(Integer reviewID, String newComment);
    void updateReviewRating(Integer reviewID, BigDecimal newRating);
    void updateReview(Integer reviewID, String newComment, BigDecimal newRating);
}
