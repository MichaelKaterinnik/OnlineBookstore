package com.onlinebookstore.services;

import com.onlinebookstore.dao.BookDao;
import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.ReviewEntity;
import com.onlinebookstore.models.BookDTO;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
public class BooksServiceImpl implements BooksService {
    @Autowired
    private BookDao booksRepository;
    @Autowired
    private AuthorsService authorsService;
    @Autowired
    private CollectionBooksService collectionBooksService;
    @Autowired
    private ReviewsService reviewsService;

    private ModelMapper modelMapper;


    public BookEntity createBook() {
        return new BookEntity();
    }

    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public BookEntity addNewBook(BookDTO book) {
        BookEntity newBook = createBook();
        newBook.setTitle(book.getTitle());
        newBook.setDescription(book.getDescription());

        // визначаємо автора
        AuthorEntity author = null;
        author = authorsService.findAuthorsByFirstAndLastName(book.getAuthorFirstName(), book.getAuthorLastName());

        if (author != null) {
            author = authorsService.findAuthorsByFirstAndLastName(book.getAuthorFirstName(), book.getAuthorLastName());
            newBook.setAuthorId(author.getId());
        } else {
            authorsService.addNewAuthorForNewBook(book.getAuthorFirstName(), book.getAuthorLastName());
        }

        // визначаємо жанри книги
        collectionBooksService.definingNewBookGenres(book, newBook);

        newBook.setPrice(book.getPrice());
        newBook.setQuantity(book.getQuantity());
        newBook.setAvailability(true);
        newBook.setCoverImage(book.getCoverImage());
        booksRepository.save(newBook);
        return newBook;
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
    public List<BookEntity> findBooksByAuthor(AuthorEntity author, Pageable pageable) {
        return booksRepository.findAllByAuthorOrderByAvailabilityDesc(author);
    }
    public List<BookDTO> getBooksDTOByAuthor(AuthorEntity author, Pageable pageable) {
        List<BookEntity> bookEntities = booksRepository.findAllByAuthorOrderByAvailabilityDesc(author);
        return bookEntities.stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class))
                .collect(Collectors.toList());
    }

    public List<BookEntity> findBooksByAuthorLastName(String lastName, Pageable pageable) {
        return booksRepository.findAllByAuthorLastNameContainingIgnoreCaseOrderByAvailabilityDesc(lastName);
    }
    public List<BookDTO> getBooksDTOByAuthorLastName(String lastName, Pageable pageable) {
        List<BookEntity> bookEntities = booksRepository.findAllByAuthorLastNameContainingIgnoreCaseOrderByAvailabilityDesc(lastName);
        return bookEntities.stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class))
                .collect(Collectors.toList());
    }
    public List<BookEntity> findBooksByAuthorFirstAndLastNames(String firstName, String lastName, Pageable pageable) {
        return booksRepository.findAllByAuthorFirstNameContainingIgnoreCaseOrAuthorLastNameContainingIgnoreCase(firstName, lastName);
    }
    public List<BookDTO> getBooksDTOByAuthorFirstAndLastNames(String firstName, String lastName, Pageable pageable) {
        List<BookEntity> bookEntities = booksRepository.findAllByAuthorFirstNameContainingIgnoreCaseOrAuthorLastNameContainingIgnoreCase(firstName, lastName);
        return bookEntities.stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class))
                .collect(Collectors.toList());
    }
    public List<BookEntity> findBooksByTitle(String title, Pageable pageable) {
        return booksRepository.findAllByTitleContainingIgnoreCase(title);
    }
    public List<BookDTO> getBooksDTOByTitle(String title, Pageable pageable) {
        List<BookEntity> bookEntities = booksRepository.findAllByTitleContainingIgnoreCase(title);
        return bookEntities.stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class))
                .collect(Collectors.toList());
    }
    public List<BookEntity> findBooksByCollectionID(Integer id) {
        return booksRepository.findBooksByCollectionId(id);
    }
    public List<BookEntity> findBooksByCategory(String collectionName, Pageable pageable) {
        return booksRepository.findByCollectionName(collectionName);
    }
    public List<BookDTO> getBooksDTOByCategory(String collectionName, Pageable pageable) {
        List<BookEntity> bookEntities = booksRepository.findByCollectionName(collectionName);
        return bookEntities.stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class))
                .collect(Collectors.toList());
    }
    public List<BookEntity> findBooksByOrderId(Integer orderId) {
        return booksRepository.findBooksByOrderId(orderId);
    }
    public BookDTO getBookDTOByID(Integer id) throws EntityNotFoundException {
        Optional<BookEntity> optionalBook = booksRepository.findById(id);
        if (optionalBook.isPresent()) {
            BookEntity bookEntity = optionalBook.get();
            BookDTO bookDTO = new BookDTO();
            bookDTO.setTitle(bookEntity.getTitle());
            bookDTO.setDescription(bookEntity.getDescription());
            bookDTO.setAuthorFirstName(bookEntity.getAuthor().getFirstName());
            bookDTO.setAuthorLastName(bookEntity.getAuthor().getLastName());
            bookDTO.setPrice(bookEntity.getPrice());
            bookDTO.setQuantity(bookEntity.getQuantity());
            bookDTO.setCoverImage(bookEntity.getCoverImage());
            return bookDTO;
        } else {
            throw new EntityNotFoundException();
        }
    }
    public BookEntity findBookByID(Integer id) {
        Optional<BookEntity> optionalBook = booksRepository.findById(id);
        return optionalBook.orElse(null);
    }
    public List< BookEntity> findPopularBooks(Pageable pageable) {
        return booksRepository.findPopularBooks();
    }
    public List<BookDTO> getPopularBooksDTO(Pageable pageable) {
        List<BookEntity> bookEntities = booksRepository.findPopularBooks();
        return bookEntities.stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class))
                .collect(Collectors.toList());
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
    public List<BookDTO> filterBooksByPriceRange(List<BookDTO> books, BigDecimal minPrice, BigDecimal maxPrice) {
        return books.stream()
                .filter(book -> book.getPrice().compareTo(minPrice) >= 0 && book.getPrice().compareTo(maxPrice) <= 0)
                .sorted(Comparator.comparing(BookDTO::getPrice))
                .collect(Collectors.toList());
    }
    public List<BookDTO> filterBooksByRating(List<BookDTO> books, BigDecimal minRating, BigDecimal maxRating) {
        return books.stream()
                .filter(book -> book.getRating().compareTo(minRating) >= 0 && book.getRating().compareTo(maxRating) <= 0)
                .collect(Collectors.toList());
    }
    public List<BookDTO> filterBooksByAvailability(List<BookDTO> books) {
        return books.stream()
                .filter(BookDTO::getAvailability)
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
    public void updateBookDPQA(Integer bookID, String description, BigDecimal price, Integer quantity, Boolean availability) {
        booksRepository.updateBookDPQA(bookID, description, price, quantity, availability);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookDescription(Integer bookID, String description) {
        booksRepository.updateBookDescription(bookID, description);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateBookRating(Integer bookID, Double rating) {
        BookEntity book = findBookByID(bookID);
        List<ReviewEntity> bookReviews = reviewsService.findBookReviews(book);
        double reviewSum = 0.0;
        for (ReviewEntity review : bookReviews) {
            reviewSum += review.getRating().doubleValue();
        }
        BigDecimal newRating = BigDecimal.valueOf(reviewSum + rating).divide(BigDecimal.valueOf(bookReviews.size() + 1));

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
