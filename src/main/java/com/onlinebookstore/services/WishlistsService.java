package com.onlinebookstore.services;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.WishlistBookEntity;
import com.onlinebookstore.domain.WishlistEntity;

import java.util.List;

public interface WishlistsService {

    WishlistEntity createNewWishlist(Integer userID);

    WishlistBookEntity addBookToWishlist(Integer wishlistID, Integer bookID);

    void removeBookFromWishlist(WishlistBookEntity wishlistBook);
    void removeBooksFromWishlistById(Integer wishlistItemId);

    WishlistEntity findWishlistById(Integer wishlistID);
    WishlistBookEntity findWishlistItemById(Integer wishlistItemId);
    WishlistEntity findWishlistByUserId(Integer userID);

    List<BookEntity> getBooksFromUserWishlist(Integer userId);
    List<BookEntity> getBooksByWishlistId(Integer wishlistID);
    List<WishlistBookEntity> findAllItemsInWishlist(Integer wishlistId);

    void deleteWishlist(WishlistEntity wishlist);
    void deleteWishlistById(Integer id);
    void deleteWishlistBookById(Integer integer);
    void deleteWishlistBook(WishlistBookEntity entity);
}
