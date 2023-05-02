package com.onlinebookstore.services;

import com.onlinebookstore.domain.DiscountEntity;
import com.onlinebookstore.models.DiscountDTO;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountsService {
    DiscountEntity createDiscount();

    DiscountEntity addNewDiscount(DiscountDTO discountDTO);

    void deleteDiscount(DiscountEntity discount);
    void deleteDiscountById(Integer discountID);

    DiscountEntity findDiscountById(Integer discountID);
    DiscountEntity findDiscountByCode(String discountCode);
    List<DiscountEntity> findAllOrderedByExpiredDateDesc(Pageable pageable);
    List<DiscountEntity> findAllExpiredDiscounts(Pageable pageable);
    List<DiscountEntity> findAllNonExpiredDiscounts(Pageable pageable);

    void updateDiscountCode(Integer discountID, String newCode);
    void updateDiscountDescription(Integer discountID, String newDescription);
    void updateDiscountPercentage(Integer discountID, BigDecimal newPercentage);
    void updateDiscountStartDate(Integer discountID, String newStartDate);
    void updateDiscountEndDate(Integer discountID, String newEndDate);
    void updateDiscount(Integer discountID, String newCode, String newDescription, BigDecimal newPercentage,
                        String newStartDate, String newEndDate);
}
