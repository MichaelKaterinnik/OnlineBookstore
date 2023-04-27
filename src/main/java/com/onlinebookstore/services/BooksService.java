package com.onlinebookstore.services;

import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.models.BookDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
public interface BooksService {
    BookEntity createBook();

    void addNewBook(BookDTO book);

    void deleteBookByID(Integer bookID);
    void deleteBook(BookEntity book);

    List<BookEntity> getAllBooks();
    List<BookEntity> findBooksByAuthor(AuthorEntity author);
    List<BookEntity> findBooksByAuthorLastName(String lastName);
    List<BookEntity> findBooksByAuthorFirstAndLastNames(String firstName, String lastName);
    List<BookEntity> findBooksByTitle(String title);
    List<BookEntity> findBooksByCollectionID(Integer id);
    List<BookEntity> findBooksByCategory(String collectionName);
    List<BookEntity> findBooksByOrderId(Integer orderId);

    BookEntity findBookByID(Integer id);

    ArrayList<BookEntity> orderBookListByPriceAscending(List<BookEntity> bookList);
    ArrayList<BookEntity> orderBookListByPriceDesc(List<BookEntity> bookList);
    ArrayList<BookEntity> orderBookListByRatingAsc(List<BookEntity> bookList);
    ArrayList<BookEntity> orderBookListByRatingDesc(List<BookEntity> bookList);
    List<BookEntity> filterBooksByPriceRange(List<BookEntity> books, BigDecimal minPrice, BigDecimal maxPrice);
    List<BookEntity> filterBooksByRating(List<BookEntity> books, BigDecimal minRating);
    List<BookEntity> filterBooksByAvailability(List<BookEntity> books);

    void updateBookInfo(Integer bookID, String description, BigDecimal rating, BigDecimal price, Integer quantity, Boolean availability, byte[] coverImage);
    void updateBookDescription(Integer bookID, String description);
    void updateBookRating(Integer bookID, BigDecimal newRating);
    void updateBookPrice(Integer bookID, BigDecimal newPrice);
    void updateBookQuantity(Integer bookID, Integer newQuantity);
    void updateBookAvailability(Integer bookID, Boolean availability);
    void updateBookCover(Integer bookID, byte[] newCoverImage);

}
