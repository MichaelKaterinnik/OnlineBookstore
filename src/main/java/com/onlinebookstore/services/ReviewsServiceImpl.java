package com.onlinebookstore.services;

import com.onlinebookstore.dao.ReviewDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.ReviewDTO;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
public class ReviewsServiceImpl implements ReviewsService {
    @Autowired
    private ReviewDao reviewsRepository;

    private ModelMapper modelMapper;


    public ReviewEntity createReview() {
        return new ReviewEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ReviewEntity addNewReview(ReviewDTO reviewDTO) {
        ReviewEntity newReview = createReview();
        newReview.setUserId(reviewDTO.getUserId());
        newReview.setBookId(reviewDTO.getBookId());
        newReview.setComment(reviewDTO.getComment());
        newReview.setRating(reviewDTO.getRating());
        reviewsRepository.save(newReview);
        return newReview;
    }


    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteReviewById(Integer reviewID) {
        reviewsRepository.deleteById(reviewID);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteReview(ReviewEntity review) {
        reviewsRepository.delete(review);
    }


    // get-methods:
    public ReviewEntity findReviewById(Integer reviewID) throws EntityNotFoundException {
        Optional<ReviewEntity> optionalReview = reviewsRepository.findById(reviewID);
        if (optionalReview.isPresent()) {
            return optionalReview.get();
        } else throw new EntityNotFoundException();
    }
    public ReviewDTO findReviewDTOById(Integer reviewID) {
        Optional<ReviewEntity> optionalReview = reviewsRepository.findById(reviewID);
        if (optionalReview.isPresent()) {
            ReviewEntity review = optionalReview.get();
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setId(review.getId());
            reviewDTO.setUserId(review.getUserId());
            reviewDTO.setComment(review.getComment());
            reviewDTO.setRating(review.getRating());
            reviewDTO.setCreatedAt(review.getCreatedAt());
            reviewDTO.setUpdatedAt(review.getUpdatedAt());
            return reviewDTO;
        } else throw new EntityNotFoundException();
    }

    public List<ReviewEntity> findReviewsByUserID(Integer userID, Pageable pageable) {
        return reviewsRepository.findAllByUserId(userID);
    }
    public List<ReviewDTO> findReviewsDTOByUserID(Integer userID, Pageable pageable) {
        List<ReviewEntity> reviewEntities = reviewsRepository.findAllByUserId(userID);
        return reviewEntities.stream()
                .map(reviewEntity -> modelMapper.map(reviewEntity, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    public List<ReviewEntity> findUserReviews(UserEntity user) {
        return reviewsRepository.findAllByUser(user);
    }
    public List<ReviewDTO> findUserReviewsDTO(UserEntity user) {
        List<ReviewEntity> reviewEntities = reviewsRepository.findAllByUser(user);
        return reviewEntities.stream()
                .map(reviewEntity -> modelMapper.map(reviewEntity, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    public List<ReviewEntity> findReviewsByBookId(Integer bookID, Pageable pageable) {
        return reviewsRepository.findAllByBookId(bookID);
    }
    public List<ReviewDTO> findReviewsDTOByBookId(Integer bookID, Pageable pageable) {
        List<ReviewEntity> reviewEntities = reviewsRepository.findAllByBookId(bookID);
        return reviewEntities.stream()
                .map(reviewEntity -> modelMapper.map(reviewEntity, ReviewDTO.class))
                .collect(Collectors.toList());
    }

    public List<ReviewEntity> findBookReviews(BookEntity book) {
        return reviewsRepository.findAllByBook(book);
    }


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateReviewComment(Integer reviewID, String newComment) {
        reviewsRepository.updateReviewComment(reviewID, newComment);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateReviewRating(Integer reviewID, BigDecimal newRating) {
        reviewsRepository.updateReviewRating(reviewID, newRating);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateReview(Integer reviewID, String newComment, BigDecimal newRating) {
        reviewsRepository.updateReview(reviewID, newComment, newRating);
    }
}
