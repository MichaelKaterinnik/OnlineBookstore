package com.onlinebookstore.dao;

import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.domain.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface BookDao extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findAll();

    List<BookEntity> findAllByTitleContainingIgnoreCase(String title);

    List<BookEntity> findAllByAuthor(AuthorEntity author);

    @Query("SELECT b FROM BookEntity b JOIN b.collections c WHERE c.id = :collectionId")
    List<BookEntity> findBooksByCollectionId(@Param("collectionId") Integer collectionId);

    @Query("SELECT b FROM BookEntity b JOIN b.collections c WHERE c.name = :collectionName")
    List<BookEntity> findByCollectionName(@Param("collectionName") String collectionName);

    @Query("SELECT oi.book FROM OrderItemEntity oi WHERE oi.order.id = :orderId")
    List<BookEntity> findBooksByOrderId(@Param("orderId") Integer orderId);

    // робимо список книг, відсортованих за популярністю (кількістю додавань у вішлісти + замовлень)
    @Query("SELECT b, COUNT(DISTINCT wb), COUNT(DISTINCT oi) " +
            "FROM BookEntity b " +
            "LEFT JOIN WishlistBookEntity wb ON b.id = wb.bookId " +
            "LEFT JOIN OrderItemEntity oi ON b.id = oi.bookId " +
            "GROUP BY b.id " +
            "ORDER BY COUNT(DISTINCT wb) DESC, COUNT(DISTINCT oi) DESC")
    List<BookEntity> findPopularBooks();


    Optional<BookEntity> findById(Integer id);


    @Override
    <S extends BookEntity> S save(S entity);


    @Override
    void deleteById(Integer integer);
}
