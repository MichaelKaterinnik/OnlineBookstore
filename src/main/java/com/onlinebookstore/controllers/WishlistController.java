package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.domain.WishlistBookEntity;
import com.onlinebookstore.domain.WishlistEntity;
import com.onlinebookstore.services.BooksService;
import com.onlinebookstore.services.UsersService;
import com.onlinebookstore.services.WishlistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_wishlist")
public class WishlistController {
    @Autowired
    private WishlistsService wishlistsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private BooksService booksService;


    // USER, ADMIN
    @PostMapping("/add")
    public ResponseEntity<WishlistEntity> createUserWishlist(@RequestBody Integer userID) {
        UserEntity user = usersService.findById(userID);
        WishlistEntity createdWishlist = wishlistsService.createNewWishlist(userID);
        System.out.println("Створено список бажаних покупок для користувача " + user.getFirstName() + " " + user.getLastName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWishlist);
    }

    // USER, ADMIN
    @PostMapping("/add_book")
    public ResponseEntity<WishlistBookEntity> addBookToWishlist(@RequestBody Integer userID, Integer bookID) {
        BookEntity wishlistedBook = booksService.findBookByID(bookID);
        UserEntity user = usersService.findById(userID);

        WishlistEntity userWishlist = wishlistsService.findWishlistByUserId(userID);
        WishlistBookEntity newWishlistBook = wishlistsService.addBookToWishlist(userWishlist.getId(), bookID);

        System.out.println("Книгу " + wishlistedBook.getTitle() + " додано до списку бажань користувача " + user.getFirstName() + " " + user.getLastName());
        return ResponseEntity.status(HttpStatus.CREATED).body(newWishlistBook);
    }


    // USER, ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<WishlistEntity> getUserWishlistByUserId(@PathVariable Integer userID) {
        WishlistEntity userWishlist = wishlistsService.findWishlistByUserId(userID);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(userWishlist, headers, HttpStatus.OK);
    }

    // USER, ADMIN
    @GetMapping("/wishlist_books")
    public ResponseEntity<List<BookEntity>> getUserWishlistBooks(@PathVariable Integer userID) {
        List<BookEntity> userWishlistBooks = wishlistsService.getBooksFromUserWishlist(userID);

        if (userWishlistBooks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(userWishlistBooks, headers, HttpStatus.OK);
    }


    // USER, ADMIN
    @DeleteMapping("wishlist_books/delete/{id}")
    public ResponseEntity<Object> deleteWishlistBookById(@PathVariable Integer bookID, Integer wishlistID) {
        List<BookEntity> books = wishlistsService.getBooksByWishlistId(wishlistID);
        BookEntity bookToDelete = null;
        for (BookEntity book : books) {
            if (book.getId().equals(bookID)) {
                bookToDelete = book;
                break;
            }
        }
        if (bookToDelete == null) {
            return ResponseEntity.notFound().build();
        }
        List<WishlistBookEntity> wishlistedBooks = wishlistsService.findAllItemsInWishlist(wishlistID);
        for (WishlistBookEntity wlbook : wishlistedBooks) {
            if (wlbook.getBookId().equals(bookToDelete.getId())) {
                wishlistsService.deleteWishlistBook(wlbook);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    // USER, ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteWishlistById(@PathVariable Integer wishlistID) {
        wishlistsService.deleteWishlistById(wishlistID);
        return ResponseEntity.ok().build();
    }
}
