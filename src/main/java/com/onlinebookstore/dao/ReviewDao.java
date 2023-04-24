package com.onlinebookstore.dao;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

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


    @Override
    <S extends ReviewEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);
    @Override
    void delete(ReviewEntity entity);
}
