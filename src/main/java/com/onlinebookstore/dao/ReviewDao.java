package com.onlinebookstore.dao;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface ReviewDao extends JpaRepository<ReviewEntity, Integer> {
    List<ReviewEntity> findAll();

    List<ReviewEntity> findAllByUserId(Integer id);
    List<ReviewEntity> findAllByUser(UserEntity user);
    List<ReviewEntity> findAllByBookId(Integer id);
    List<ReviewEntity> findAllByBook(BookEntity book);

    Optional<ReviewEntity> findById(Integer id);


    @Modifying
    @Query("UPDATE ReviewEntity r SET r.comment = :comment, r.rating = :rating WHERE r.id = :id")
    void updateReview(@Param("id") Integer id, @Param("comment") String comment, @Param("rating") BigDecimal rating);

    @Modifying
    @Query("UPDATE ReviewEntity r SET r.comment = :comment WHERE r.id = :id")
    void updateReviewComment(@Param("id") Integer id, @Param("comment") String comment);

    @Modifying
    @Query("UPDATE ReviewEntity r SET r.rating = :rating WHERE r.id = :id")
    void updateReviewRating(@Param("id") Integer id, @Param("rating") BigDecimal rating);


    @Override
    <S extends ReviewEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);
    @Override
    void delete(ReviewEntity entity);
}
