package com.onlinebookstore.dao;

import com.onlinebookstore.domain.BookDiscountEntity;
import com.onlinebookstore.domain.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface BookDiscountDao extends JpaRepository<BookDiscountEntity, Integer> {

    @Override
    List<BookDiscountEntity> findAll();

    Optional<BookDiscountEntity> findById(Integer id);
    Optional<BookDiscountEntity> findByBookId(Integer id);
    Optional<BookDiscountEntity> findByBook(BookEntity book);



    @Modifying
    @Query("UPDATE BookDiscountEntity bd SET bd.discountId = :discountId WHERE bd.id = :id")
    void updateDiscountId(@Param("id") Integer id, @Param("discountId") Integer discountId);


    @Override
    <S extends BookDiscountEntity> S save(S entity);

    @Override
    void delete(BookDiscountEntity entity);
    @Override
    void deleteById(Integer id);
}
