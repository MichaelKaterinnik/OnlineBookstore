package com.onlinebookstore.services;

import com.onlinebookstore.dao.ReviewDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.ReviewDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ReviewsServiceImpl implements ReviewsService {
    @Autowired
    private ReviewDao reviewsRepository;


    public ReviewEntity createReview() {
        return new ReviewEntity();
    }


    public void addNewReview(ReviewDTO reviewDTO) {
        ReviewEntity newReview = createReview();
        newReview.setUserId(reviewDTO.getUserId());
        newReview.setBookId(reviewDTO.getBookId());
        newReview.setComment(reviewDTO.getComment());
        newReview.setRating(reviewDTO.getRating());
        reviewsRepository.save(newReview);
    }

    public void deleteReviewById(Integer reviewID) {
        reviewsRepository.deleteById(reviewID);
    }
    public void deleteReview(ReviewEntity review) {
        reviewsRepository.delete(review);
    }

    public ReviewEntity findReviewById(Integer reviewID) throws EntityNotFoundException {
        Optional<ReviewEntity> optionalReview = reviewsRepository.findById(reviewID);
        if (optionalReview.isPresent()) {
            return optionalReview.get();
        } else throw new EntityNotFoundException();
    }
    public List<ReviewEntity> findReviewsByUserID(Integer userID) {
        return reviewsRepository.findAllByUserId(userID);
    }
    public List<ReviewEntity> findUserReviews(UserEntity user) {
        return reviewsRepository.findAllByUser(user);
    }
    public List<ReviewEntity> findReviewsByBookId(Integer bookID) {
        return reviewsRepository.findAllByBookId(bookID);
    }
    public List<ReviewEntity> findBookReviews(BookEntity book) {
        return reviewsRepository.findAllByBook(book);
    }


    public void updateReviewComment(Integer reviewID, String newComment) {
        reviewsRepository.updateReviewComment(reviewID, newComment);
    }
    public void updateReviewRating(Integer reviewID, BigDecimal newRating) {
        reviewsRepository.updateReviewRating(reviewID, newRating);
    }
    public void updateReview(Integer reviewID, String newComment, BigDecimal newRating) {
        reviewsRepository.updateReview(reviewID, newComment, newRating);
    }
}
