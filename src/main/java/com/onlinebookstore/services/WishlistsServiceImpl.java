package com.onlinebookstore.services;

import com.onlinebookstore.dao.WishlistBookDao;
import com.onlinebookstore.dao.WishlistDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.WishlistBookEntity;
import com.onlinebookstore.domain.WishlistEntity;
import com.onlinebookstore.models.BookDTO;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service-class services both Wishlists and WishlistBooks entities
 */
@Component
@Service
public class WishlistsServiceImpl implements WishlistsService {
    @Autowired
    private WishlistDao wishlistsRepository;
    @Autowired
    private WishlistBookDao wishlistBooksRepository;

    private ModelMapper modelMapper;


    public WishlistEntity createWishlist() {
        return new WishlistEntity();
    }
    public WishlistBookEntity createWishlistBook() {
        return new WishlistBookEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public WishlistEntity createNewWishlist(Integer userID) {
        WishlistEntity userWishlist = createWishlist();
        userWishlist.setUserId(userID);
        wishlistsRepository.save(userWishlist);
        return userWishlist;
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public WishlistBookEntity addBookToWishlist(Integer wishlistID, Integer bookID) {
        WishlistBookEntity newWishlistBook = createWishlistBook();
        newWishlistBook.setWishlistId(wishlistID);
        newWishlistBook.setBookId(bookID);
        wishlistBooksRepository.save(newWishlistBook);
        return newWishlistBook;
    }


    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeBookFromWishlist(WishlistBookEntity wishlistBook) {
        wishlistBooksRepository.delete(wishlistBook);
    }
    public void removeBooksFromWishlistById(Integer wishlistItemId) {
        wishlistBooksRepository.deleteById(wishlistItemId);
    }


    // get-methods:
    public WishlistEntity findWishlistById(Integer wishlistID) throws EntityNotFoundException {
        Optional<WishlistEntity> optionalWishlist = wishlistsRepository.findById(wishlistID);
        return optionalWishlist.orElse(null);
    }
    public WishlistBookEntity findWishlistItemById(Integer wishlistItemId) throws EntityNotFoundException {
        Optional<WishlistBookEntity> optionalWishlistBook = wishlistBooksRepository.findById(wishlistItemId);
        return optionalWishlistBook.orElse(null);
    }
    public WishlistEntity findWishlistByUserId(Integer userID) throws EntityNotFoundException {
        Optional<WishlistEntity> optionalWishlist = wishlistsRepository.findById(userID);
        return optionalWishlist.orElse(null);
    }

    public List<BookEntity> getBooksFromUserWishlist(Integer userId) {
        return wishlistBooksRepository.findBooksByUserId(userId);
    }
    public List<BookDTO> getBooksDTOFromUserWishlist(Integer userId) {
        List<BookEntity> bookEntities = wishlistBooksRepository.findBooksByUserId(userId);
        return bookEntities.stream()
                .map(bookEntity -> modelMapper.map(bookEntity, BookDTO.class))
                .collect(Collectors.toList());
    }

    public List<BookEntity> getBooksByWishlistId(Integer wishlistID) {
        return wishlistBooksRepository.findBooksByWishlistId(wishlistID);
    }
    public List<WishlistBookEntity> findAllItemsInWishlist(Integer wishlistId) {
        return wishlistBooksRepository.findAllByWishlistId(wishlistId);
    }


    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteWishlist(WishlistEntity wishlist) {
        wishlistsRepository.delete(wishlist);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteWishlistById(Integer id) {
        wishlistsRepository.deleteById(id);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteWishlistBookById(Integer id) {
        wishlistBooksRepository.deleteById(id);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteWishlistBook(WishlistBookEntity wishlistBook) {
        wishlistBooksRepository.delete(wishlistBook);
    }


}
