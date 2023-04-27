package com.onlinebookstore.services;

import com.onlinebookstore.domain.BookDiscountEntity;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderItemEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Component
@Service
public interface BookDiscountsService {
    BookDiscountEntity createBookDiscount();

    void addNewBookDiscount(Integer bookID, Integer discountID);

    BookDiscountEntity findBookDiscountById(Integer bookDiscountID);
    BookDiscountEntity findBookDiscountByBook(BookEntity book);
    BookDiscountEntity findBookDiscountByBookId(Integer bookID);

    void updateDiscountIDForBookDiscount(Integer bookDiscountID, Integer newDiscountID);

    void applyDiscountToBook(Integer bookID);
    BigDecimal getBookDiscountedPrice(Integer bookID);
    void applyBookDiscountWhenOrdering(OrderItemEntity newOrderItem, BookEntity orderingBook);
    boolean ifBookIsDiscounted(Integer bookID);

}
