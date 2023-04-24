package com.onlinebookstore.dao;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.WishlistBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface WishlistBookDao extends JpaRepository<WishlistBookEntity, Integer> {
    @Override
    Optional<WishlistBookEntity> findById(Integer integer);


    List<WishlistBookEntity> findAllByWishlistId(Integer wishlistID);

    @Query("SELECT wi.book FROM WishlistBookEntity wi WHERE wi.wishlistId = :wishlistID")
    List<BookEntity> findBooksByWishlistId(@Param("wishlistID") Integer wishlistID);

    @Query("SELECT wb.book FROM WishlistBookEntity wb WHERE wb.wishlist.user.id = :userId")
    List<BookEntity> findBooksByUserId(@Param("userId") Integer userId);




    @Override
    <S extends WishlistBookEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);

    @Override
    void delete(WishlistBookEntity entity);
}
