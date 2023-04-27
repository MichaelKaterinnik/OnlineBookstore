package com.onlinebookstore.services;

import com.onlinebookstore.domain.DiscountEntity;
import com.onlinebookstore.models.DiscountDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Component
@Service
public interface DiscountsService {
    DiscountEntity createDiscount();

    DiscountEntity addNewDiscount(DiscountDTO discountDTO);

    void deleteDiscount(DiscountEntity discount);
    void deleteDiscountById(Integer discountID);

    DiscountEntity findDiscountById(Integer discountID);
    DiscountEntity findDiscountByCode(String discountCode);

    void updateDiscountCode(Integer discountID, String newCode);
    void updateDiscountDescription(Integer discountID, String newDescription);
    void updateDiscountPercentage(Integer discountID, BigDecimal newPercentage);
    void updateDiscountStartDate(Integer discountID, String newStartDate);
    void updateDiscountEndDate(Integer discountID, String newEndDate);
    void updateDiscount(Integer discountID, String newCode, String newDescription, BigDecimal newPercentage,
                        String newStartDate, String newEndDate);
}
