package com.onlinebookstore.services;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.ReviewDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Component
@Service
public interface ReviewsService {

    void addNewReview(ReviewDTO reviewDTO);

    void deleteReviewById(Integer reviewID);
    void deleteReview(ReviewEntity review);

    ReviewEntity findReviewById(Integer reviewID);
    List<ReviewEntity> findReviewsByUserID(Integer userID);
    List<ReviewEntity> findUserReviews(UserEntity user);
    List<ReviewEntity> findReviewsByBookId(Integer bookID);
    List<ReviewEntity> findBookReviews(BookEntity book);

    void updateReviewComment(Integer reviewID, String newComment);
    void updateReviewRating(Integer reviewID, BigDecimal newRating);
    void updateReview(Integer reviewID, String newComment, BigDecimal newRating);
}
