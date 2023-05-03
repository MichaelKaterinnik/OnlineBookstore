package com.onlinebookstore.services;

import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.models.BookDTO;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface BooksService {
    BookEntity createBook();

    BookEntity addNewBook(BookDTO book);

    void deleteBookByID(Integer bookID);
    void deleteBook(BookEntity book);

    List<BookEntity> getAllBooks();
    List<BookEntity> findBooksByAuthor(AuthorEntity author, Pageable pageable);
    List<BookDTO> getBooksDTOByAuthor(AuthorEntity author, Pageable pageable);

    List<BookEntity> findBooksByAuthorLastName(String lastName, Pageable pageable);
    List<BookDTO> getBooksDTOByAuthorLastName(String lastName, Pageable pageable);

    List<BookEntity> findBooksByAuthorFirstAndLastNames(String firstName, String lastName, Pageable pageable);
    List<BookDTO> getBooksDTOByAuthorFirstAndLastNames(String firstName, String lastName, Pageable pageable);

    List<BookEntity> findBooksByTitle(String title, Pageable pageable);
    List<BookDTO> getBooksDTOByTitle(String title, Pageable pageable);

    List<BookEntity> findBooksByCollectionID(Integer id);
    List<BookEntity> findBooksByCategory(String collectionName, Pageable pageable);
    List<BookDTO> getBooksDTOByCategory(String collectionName, Pageable pageable);

    List<BookEntity> findBooksByOrderId(Integer orderId);
    List<BookEntity> findPopularBooks(Pageable pageable);
    List<BookDTO> getPopularBooksDTO(Pageable pageable);


    BookDTO getBookDTOByID(Integer id);
    BookEntity findBookByID(Integer id);

    ArrayList<BookEntity> orderBookListByPriceAscending(List<BookEntity> bookList);
    ArrayList<BookEntity> orderBookListByPriceDesc(List<BookEntity> bookList);
    ArrayList<BookEntity> orderBookListByRatingAsc(List<BookEntity> bookList);
    ArrayList<BookEntity> orderBookListByRatingDesc(List<BookEntity> bookList);
    List<BookDTO> filterBooksByPriceRange(List<BookDTO> books, BigDecimal minPrice, BigDecimal maxPrice);
    List<BookDTO> filterBooksByRating(List<BookDTO> books, BigDecimal minRating, BigDecimal maxRating);
    List<BookDTO> filterBooksByAvailability(List<BookDTO> books);

    void updateBookInfo(Integer bookID, String description, BigDecimal rating, BigDecimal price, Integer quantity, Boolean availability, byte[] coverImage);
    void updateBookDPQA(Integer bookID, String description, BigDecimal price, Integer quantity, Boolean availability);
    void updateBookDescription(Integer bookID, String description);
    void updateBookRating(Integer bookID, Double newRating);
    void updateBookPrice(Integer bookID, BigDecimal newPrice);
    void updateBookQuantity(Integer bookID, Integer newQuantity);
    void updateBookAvailability(Integer bookID, Boolean availability);
    void updateBookCover(Integer bookID, byte[] newCoverImage);

}
