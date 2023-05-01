package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.models.BookDTO;
import com.onlinebookstore.services.BooksService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BooksService booksService;

    // GUEST, USER, ADMIN
    @GetMapping("/collection_books")
    public ResponseEntity<List<BookEntity>> findBooksByCollectionName(@RequestParam("collectionName") String collectionName,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "20") int size,
                                                                      @RequestParam(required = false) BigDecimal priceFrom,
                                                                      @RequestParam(required = false) BigDecimal priceTo,
                                                                      @RequestParam(required = false) BigDecimal ratingFrom,
                                                                      @RequestParam(required = false) BigDecimal ratingTo,
                                                                      @RequestParam(required = false) Boolean availability,
                                                                      @RequestParam(required = false) String sort,
                                                                      @RequestParam(required = false) String direction) {
        Pageable pageable = PageRequest.of(page, size);
        List<BookEntity> collectionBooks = null;
        try {
            collectionBooks = booksService.findBooksByCategory(collectionName, pageable);
        } catch (EntityNotFoundException e) {
            // throw new RuntimeException(e);
        }
        return getListSortedAndFiltered(page, size, priceFrom, priceTo, ratingFrom, ratingTo, availability, sort, direction, collectionBooks);
    }

    // GUEST, USER, ADMIN
    @GetMapping("/books_search")
    public ResponseEntity<List<BookEntity>> findBooksByTitle(@RequestParam("title") String title,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size,
                                                             @RequestParam(required = false) BigDecimal priceFrom,
                                                             @RequestParam(required = false) BigDecimal priceTo,
                                                             @RequestParam(required = false) BigDecimal ratingFrom,
                                                             @RequestParam(required = false) BigDecimal ratingTo,
                                                             @RequestParam(required = false) Boolean availability,
                                                             @RequestParam(required = false) String sort,
                                                             @RequestParam(required = false) String direction) {
        Pageable pageable = PageRequest.of(page, size);
        List<BookEntity> resultBooks = booksService.findBooksByTitle(title, pageable);

        return getListSortedAndFiltered(page, size, priceFrom, priceTo, ratingFrom, ratingTo, availability, sort, direction, resultBooks);
    }

    // GUEST, USER, ADMIN
    @GetMapping("/books_by_author")
    public ResponseEntity<List<BookEntity>> findBooksByAuthor(@RequestParam("author") String author,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size,
                                                              @RequestParam(required = false) BigDecimal priceFrom,
                                                              @RequestParam(required = false) BigDecimal priceTo,
                                                              @RequestParam(required = false) BigDecimal ratingFrom,
                                                              @RequestParam(required = false) BigDecimal ratingTo,
                                                              @RequestParam(required = false) Boolean availability,
                                                              @RequestParam(required = false) String sort,
                                                              @RequestParam(required = false) String direction) {
        Pageable pageable = PageRequest.of(page, size);
        List<BookEntity> books;
        if (author.trim().split("\\s+").length == 1) {
            books = booksService.findBooksByAuthorLastName(author.trim(), pageable);
        } else {
            String[] names = author.trim().split("\\s+");
            books = booksService.findBooksByAuthorFirstAndLastNames(names[0], names[1], pageable);
            if (books.isEmpty()) {
                books = booksService.findBooksByAuthorFirstAndLastNames(names[1], names[0], pageable);
            }
        }

        return getListSortedAndFiltered(page, size, priceFrom, priceTo, ratingFrom, ratingTo, availability, sort, direction, books);
    }

    // GUEST, USER, ADMIN
    @GetMapping("/get/{id}")
    public ResponseEntity<BookEntity> findBookById(@PathVariable Integer id) {
        BookEntity resultBook = booksService.findBookByID(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(resultBook, headers, HttpStatus.OK);
    }


    // ADMIN
    @PostMapping("/add")
    public ResponseEntity<BookEntity> addBook(@RequestBody BookDTO bookDTO) {
        BookEntity createdBook = booksService.addNewBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    // ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteBookById(@PathVariable Integer bookID) {
        booksService.deleteBookByID(bookID);
        return ResponseEntity.ok().body("Книга була успішно видалена з бази.");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteBook(@PathVariable BookEntity book) {
        booksService.deleteBook(book);
        return ResponseEntity.ok().body("Книга була успішно видалена з бази.");
    }

    // ADMIN
    @PutMapping("/update")
    public ResponseEntity<Object> updateBook(@RequestBody BookDTO bookDTO, @PathVariable Integer bookID) {
        booksService.updateBookInfo(bookID, bookDTO.getDescription(), bookDTO.getRating(), bookDTO.getPrice(), bookDTO.getQuantity(), bookDTO.getAvailability(), bookDTO.getCoverImage());
        return ResponseEntity.ok().body("Інформацію оновлено!");
    }
    @PutMapping("/updateDPKA")
    public ResponseEntity<Object> updateBookDPQA(@RequestBody BookDTO bookDTO, @PathVariable Integer bookID) {
        booksService.updateBookDPQA(bookID, bookDTO.getDescription(), bookDTO.getRating(), bookDTO.getQuantity(), bookDTO.getAvailability());
        return ResponseEntity.ok().body("Інформацію оновлено!");
    }
    @PutMapping("/update_description")
    public ResponseEntity<Object> updateBookDescription(@RequestBody String description, @PathVariable Integer bookID) {
        booksService.updateBookDescription(bookID, description);
        return ResponseEntity.ok().body("Опис книги оновлено!");
    }
    @PutMapping("/update_rating")
    public ResponseEntity.BodyBuilder updateBookRating(@RequestBody Double newRating, @PathVariable Integer bookID) {
        booksService.updateBookRating(bookID, newRating);
        return ResponseEntity.ok();
    }
    @PutMapping("/update_price")
    public ResponseEntity<Object> updateBookPrice(@RequestBody BigDecimal newPrice, @PathVariable Integer bookID) {
        booksService.updateBookPrice(bookID, newPrice);
        return ResponseEntity.ok().body("Вартість книги оновлено!");
    }
    @PutMapping("/update_quantity")
    public ResponseEntity<Object> updateBookQuantity(@RequestBody Integer newQuantity, @PathVariable Integer bookID) {
        booksService.updateBookQuantity(bookID, newQuantity);
        return ResponseEntity.ok().body("Кількість доступних екземплярів книги оновлено!");
    }
    @PutMapping("/update_availability")
    public ResponseEntity.BodyBuilder updateBookAvailability(@RequestBody Boolean availability, @PathVariable Integer bookID) {
        booksService.updateBookAvailability(bookID, availability);
        return ResponseEntity.ok();
    }
    @PutMapping("/update_cover")
    public ResponseEntity<Object> updateBookCover(@RequestBody byte[] newCover, @PathVariable Integer bookID) {
        booksService.updateBookCover(bookID, newCover);
        return ResponseEntity.ok().body("Обкладинку книги оновлено!");
    }



    private ResponseEntity<List<BookEntity>> getListSortedAndFiltered(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "20") int size,
                                                                      @RequestParam(required = false) BigDecimal priceFrom,
                                                                      @RequestParam(required = false) BigDecimal priceTo,
                                                                      @RequestParam(required = false) BigDecimal ratingFrom,
                                                                      @RequestParam(required = false) BigDecimal ratingTo,
                                                                      @RequestParam(required = false) Boolean availability,
                                                                      @RequestParam(required = false) String sort,
                                                                      @RequestParam(required = false) String direction,
                                                                      List<BookEntity> collectionBooks) {
        Pageable pageable;
        if (priceFrom != null && priceTo != null) {
            booksService.filterBooksByPriceRange(collectionBooks, priceFrom, priceTo);
        }
        if (ratingFrom != null && ratingTo != null) {
            booksService.filterBooksByRating(collectionBooks, ratingFrom, ratingTo);
        }
        if (availability != null) {
            booksService.filterBooksByAvailability(collectionBooks);
        }

        if (sort != null && sort.equalsIgnoreCase("price")) {
            if (direction != null && direction.equalsIgnoreCase("desc")) {
                pageable = PageRequest.of(page, size, Sort.by("price").descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by("price").ascending());
            }
        }
        if (sort != null && sort.equalsIgnoreCase("rating")) {
            if (direction != null && direction.equalsIgnoreCase("desc")) {
                pageable = PageRequest.of(page, size, Sort.by("rating").descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by("rating").ascending());
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(collectionBooks, headers, HttpStatus.OK);
    }
}
