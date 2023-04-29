package com.onlinebookstore.services;

import com.onlinebookstore.dao.BookDao;
import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.BookDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BooksServiceImpl implements BooksService {
    @Autowired
    private BookDao booksRepository;
    @Autowired
    private AuthorsService authorsService;
    @Autowired
    private CollectionsService collectionsService;
    @Autowired
    private CollectionBooksService collectionBooksService;
    @Autowired
    private EntityManager entityManager;


    public BookEntity createBook() {
        return new BookEntity();
    }

    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addNewBook(BookDTO book) {
        BookEntity newBook = createBook();
        book.setTitle(book.getTitle());
        newBook.setDescription(book.getDescription());

        // визначаємо автора
        AuthorEntity author = authorsService.findAuthorsByFirstAndLastName(book.getAuthorFirstName(), book.getAuthorLastName());
        if (author != null) {
            newBook.setAuthorId(author.getId());
        } else {
            authorsService.addNewAuthorForNewBook(book.getAuthorFirstName(), book.getAuthorLastName());
        }

        // визначаємо жанри книги
        CollectionEntity newBookGenre;
        for (String genre : book.getGenres()) {
            CollectionEntity thisBookGenre = collectionsService.findCollectionByName(genre);
            if (genre != null) {
                newBookGenre = collectionBooksService.setCollectionForNewBook(thisBookGenre.getId(), newBook.getId());
            } else {
                newBookGenre = collectionsService.addNewCollectionForNewBook(genre);
            }
            newBook.getCollections().add(newBookGenre);
            newBookGenre.getBooks().add(newBook);
            entityManager.merge(newBook);
            entityManager.merge(newBookGenre);
        }

        newBook.setPrice(book.getPrice());
        newBook.setQuantity(book.getQuantity());
        newBook.setAvailability(true);
        newBook.setCoverImage(book.getCoverImage());
        booksRepository.save(newBook);
    }

    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteBookByID(Integer bookID) {
        booksRepository.deleteById(bookID);
    }
    public void deleteBook(BookEntity book) {
        booksRepository.delete(book);
    }



    /**
     * Методи для пошуку книг за різними параметрами:
     */
    public List<BookEntity> getAllBooks() {
        return booksRepository.findAll();
    }
    public List<BookEntity> findBooksByAuthor(AuthorEntity author) {
        return booksRepository.findAllByAuthorOrderByAvailabilityDesc(author);
    }
    public List<BookEntity> findBooksByAuthorLastName(String lastName) throws IllegalArgumentException {
        if (lastName.length() < 2) {
            throw new IllegalArgumentException();
        }
        return booksRepository.findAllByAuthorLastNameContainingIgnoreCaseOrderByAvailabilityDesc(lastName);
    }
    public List<BookEntity> findBooksByAuthorFirstAndLastNames(String firstName, String lastName) {
        return booksRepository.findAllByAuthorFirstNameContainingIgnoreCaseOrAuthorLastNameContainingIgnoreCase(firstName, lastName);
    }
    public List<BookEntity> findBooksByTitle(String title) throws IllegalArgumentException {
        if (title.length() < 3) {
            throw new IllegalArgumentException();
        }
        return booksRepository.findAllByTitleContainingIgnoreCase(title);
    }
    public List<BookEntity> findBooksByCollectionID(Integer id) {
        return booksRepository.findBooksByCollectionId(id);
    }
    public List<BookEntity> findBooksByCategory(String collectionName) throws EntityNotFoundException {
        return booksRepository.findByCollectionName(collectionName);
    }
    public List<BookEntity> findBooksByOrderId(Integer orderId) {
        return booksRepository.findBooksByOrderId(orderId);
    }
    public BookEntity findBookByID(Integer id) throws EntityNotFoundException {
        Optional<BookEntity> optionalBook = booksRepository.findById(id);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Методи для впорядкування списків книг за різними параметрами:
     */
    public ArrayList<BookEntity> orderBookListByPriceAscending(List<BookEntity> bookList) {
        return (ArrayList<BookEntity>) bookList.stream()
                .sorted(Comparator.comparing(BookEntity::getPrice))
                .collect(Collectors.toList());
    }
    public ArrayList<BookEntity> orderBookListByPriceDesc(List<BookEntity> bookList) {
        return (ArrayList<BookEntity>) bookList.stream()
                .sorted(Comparator.comparing(BookEntity::getPrice).reversed())
                .collect(Collectors.toList());
    }
    public ArrayList<BookEntity> orderBookListByRatingAsc(List<BookEntity> bookList) {
        return (ArrayList<BookEntity>) bookList.stream()
                .sorted(Comparator.comparing(BookEntity::getRating))
                .collect(Collectors.toList());
    }
    public ArrayList<BookEntity> orderBookListByRatingDesc(List<BookEntity> bookList) {
        return (ArrayList<BookEntity>) bookList.stream()
                .sorted(Comparator.comparing(BookEntity::getRating).reversed())
                .collect(Collectors.toList());
    }
    public List<BookEntity> filterBooksByPriceRange(List<BookEntity> books, BigDecimal minPrice, BigDecimal maxPrice) {
        return books.stream()
                .filter(book -> book.getPrice().compareTo(minPrice) >= 0 && book.getPrice().compareTo(maxPrice) <= 0)
                .sorted(Comparator.comparing(BookEntity::getPrice))
                .collect(Collectors.toList());
    }
    public List<BookEntity> filterBooksByRating(List<BookEntity> books, BigDecimal minRating) {
        return books.stream()
                .filter(book -> book.getRating().compareTo(minRating) > 0)
                .collect(Collectors.toList());
    }
    public List<BookEntity> filterBooksByAvailability(List<BookEntity> books) {
        return books.stream()
                .filter(BookEntity::getAvailability)
                .collect(Collectors.toList());
    }


    /**
     * Блок методів для оновлення об'єктів типу "Книга" у БД:
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookInfo(Integer bookID, String description, BigDecimal rating, BigDecimal price, Integer quantity, Boolean availability, byte[] coverImage) {
        booksRepository.updateAllBookInfo(bookID, description, rating, price, quantity, availability, coverImage);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookDescription(Integer bookID, String description) {
        booksRepository.updateBookDescription(bookID, description);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookRating(Integer bookID, BigDecimal newRating) {
        booksRepository.updateBookRating(bookID, newRating);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookPrice(Integer bookID, BigDecimal newPrice) {
        booksRepository.updateBookPrice(bookID, newPrice);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookQuantity(Integer bookID, Integer newQuantity) {
        booksRepository.updateBookQuantity(bookID, newQuantity);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookAvailability(Integer bookID, Boolean availability) {
        booksRepository.updateBookAvailability(bookID, availability);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookCover(Integer bookID, byte[] newCoverImage) {
        booksRepository.updateBookImage(bookID, newCoverImage);
    }

}
