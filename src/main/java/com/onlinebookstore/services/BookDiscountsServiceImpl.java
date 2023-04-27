package com.onlinebookstore.services;

import com.onlinebookstore.dao.BookDiscountDao;
import com.onlinebookstore.domain.BookDiscountEntity;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderItemEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

public class BookDiscountsServiceImpl implements BookDiscountsService {
    @Autowired
    private BookDiscountDao bookDiscountsRepository;
    @Autowired
    private DiscountsServiceImpl discountsServices;
    @Autowired
    private BooksServiceImpl booksService;


    public BookDiscountEntity createBookDiscount() {
        return new BookDiscountEntity();
    }


    public void addNewBookDiscount(Integer bookID, Integer discountID) {
        BookDiscountEntity newBookDiscount = createBookDiscount();
        newBookDiscount.setBookId(bookID);
        newBookDiscount.setDiscountId(discountID);
        bookDiscountsRepository.save(newBookDiscount);
    }

    public BookDiscountEntity findBookDiscountById(Integer bookDiscountID) throws EntityNotFoundException {
        Optional<BookDiscountEntity> optionalBookDiscount = bookDiscountsRepository.findById(bookDiscountID);
        if (optionalBookDiscount.isPresent()) {
            return optionalBookDiscount.get();
        } else throw new EntityNotFoundException();
    }
    public BookDiscountEntity findBookDiscountByBook(BookEntity book) throws EntityNotFoundException {
        Optional<BookDiscountEntity> optionalBookDiscount = bookDiscountsRepository.findByBook(book);
        if (optionalBookDiscount.isPresent()) {
            return optionalBookDiscount.get();
        } else throw new EntityNotFoundException();
    }
    public BookDiscountEntity findBookDiscountByBookId(Integer bookID) throws EntityNotFoundException {
        Optional<BookDiscountEntity> optionalBookDiscount = bookDiscountsRepository.findByBookId(bookID);
        if (optionalBookDiscount.isPresent()) {
            return optionalBookDiscount.get();
        } else throw new EntityNotFoundException();
    }

    public void updateDiscountIDForBookDiscount(Integer bookDiscountID, Integer newDiscountID) {
        bookDiscountsRepository.updateDiscountId(bookDiscountID, newDiscountID);
    }

    /**
     * Застосування знижки до книги. Може застосовуватись адміністратором для додавання ціни зі знижкою на сайт
     * (метод getBookDiscountedPrice) або при додаванні книги у кошик через використання методу
     * ifBookIsDiscounted(Integer bookID)
     */
    public void applyDiscountToBook(Integer bookID) {
        booksService.findBookByID(bookID).setPrice(getBookDiscountedPrice(bookID));
    }
    public BigDecimal getBookDiscountedPrice(Integer bookID) {
        BookDiscountEntity bookDiscount = findBookDiscountByBookId(bookID);
        BigDecimal discountPercentage = discountsServices.findDiscountById(bookDiscount.getDiscountId()).getDiscountPercentage();
        BookEntity discountedBook = booksService.findBookByID(bookID);
        BigDecimal oldBookPrice = discountedBook.getPrice();
        BigDecimal discountedPrice = oldBookPrice.multiply(BigDecimal.valueOf(100).subtract(discountPercentage)).divide(BigDecimal.valueOf(100));
        return discountedPrice;
    }
    public void applyBookDiscountWhenOrdering(OrderItemEntity newOrderItem, BookEntity orderingBook) {
        BookDiscountEntity bookDiscount = findBookDiscountByBookId(orderingBook.getId());
        BigDecimal discountPercentage = discountsServices.findDiscountById(bookDiscount.getDiscountId()).getDiscountPercentage();
        BigDecimal oldBookPrice = orderingBook.getPrice();
        BigDecimal discountedPrice = oldBookPrice.multiply(BigDecimal.valueOf(100).subtract(discountPercentage)).divide(BigDecimal.valueOf(100));
        newOrderItem.setBookPrice(discountedPrice);
    }
    public boolean ifBookIsDiscounted(Integer bookID) {
        return !(findBookDiscountByBookId(bookID) == null);
    }

}
