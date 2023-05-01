package com.onlinebookstore.dao;

import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.domain.BookEntity;
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
public interface BookDao extends JpaRepository<BookEntity, Integer> {

    List<BookEntity> findAll();
    List<BookEntity> findAllByTitleContainingIgnoreCase(String title);

    List<BookEntity> findAllByAuthorFirstNameContainingIgnoreCaseOrAuthorLastNameContainingIgnoreCase(String firstName, String lastName);

    List<BookEntity> findAllByAuthorLastNameContainingIgnoreCaseOrderByAvailabilityDesc(String lastName);

    List<BookEntity> findAllByAuthorOrderByAvailabilityDesc(AuthorEntity author);

    @Query("SELECT b FROM BookEntity b JOIN b.collections c WHERE c.name = :collectionName")
    List<BookEntity> findByCollectionName(@Param("collectionName") String collectionName);

    // робимо список книг, відсортованих за популярністю (кількістю додавань у вішлісти + замовлень)
    @Query(value = "SELECT b, COUNT(DISTINCT wb), COUNT(DISTINCT oi) " +
            "FROM BookEntity b " +
            "LEFT JOIN WishlistBookEntity wb ON b.id = wb.bookId " +
            "LEFT JOIN OrderItemEntity oi ON b.id = oi.bookId " +
            "GROUP BY b.id " +
            "ORDER BY COUNT(DISTINCT wb) DESC, COUNT(DISTINCT oi) DESC")
    List<BookEntity> findPopularBooks();

    @Query("SELECT b FROM BookEntity b JOIN b.collections c WHERE c.id = :collectionId")
    List<BookEntity> findBooksByCollectionId(@Param("collectionId") Integer collectionId);

    @Query("SELECT oi.book FROM OrderItemEntity oi WHERE oi.order.id = :orderId")
    List<BookEntity> findBooksByOrderId(@Param("orderId") Integer orderId);




    Optional<BookEntity> findById(Integer id);

    @Modifying
    @Query("UPDATE BookEntity b SET b.description = :description, b.rating = :rating, b.price = :price, b.quantity = :quantity, b.availability = :availability, b.coverImage = :coverImage WHERE b.id = :id")
    void updateAllBookInfo(@Param("id") Integer id, @Param("description") String description, @Param("rating") BigDecimal rating, @Param("price") BigDecimal price, @Param("quantity") Integer quantity, @Param("availability") Boolean availability, @Param("coverImage") byte[] coverImage);

    @Modifying
    @Query("UPDATE BookEntity b SET b.description = :description,b.price = :price, b.quantity = :quantity, b.availability = :availability WHERE b.id = :id")
    void updateBookDPQA(@Param("id") Integer id, @Param("description") String description, @Param("price") BigDecimal price, @Param("quantity") Integer quantity, @Param("availability") Boolean availability);

    @Modifying
    @Query("UPDATE BookEntity b SET b.description = :description WHERE b.id = :id")
    void updateBookDescription(@Param("id") Integer id, @Param("description") String description);

    @Modifying
    @Query("UPDATE BookEntity b SET b.rating = :rating WHERE b.id = :id")
    void updateBookRating(@Param("id") Integer id, @Param("rating") BigDecimal rating);

    @Modifying
    @Query("UPDATE BookEntity b SET b.price = :price WHERE b.id = :id")
    void updateBookPrice(@Param("id") Integer id, @Param("price") BigDecimal price);

    @Modifying
    @Query("UPDATE BookEntity b SET b.quantity = :quantity WHERE b.id = :id")
    void updateBookQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE BookEntity b SET b.availability = :availability WHERE b.id = :id")
    void updateBookAvailability(@Param("id") Integer id, @Param("availability") Boolean availability);

    @Modifying
    @Query("UPDATE BookEntity b SET b.coverImage = :coverImage WHERE b.id = :id")
    void updateBookImage(@Param("id") Integer id, @Param("coverImage") byte[] coverImage);



    @Override
    <S extends BookEntity> S save(S entity);


    @Override
    void deleteById(Integer integer);
}
