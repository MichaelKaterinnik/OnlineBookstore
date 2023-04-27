package com.onlinebookstore.services;

import com.onlinebookstore.dao.DiscountDao;
import com.onlinebookstore.domain.DiscountEntity;
import com.onlinebookstore.models.DiscountDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DiscountsServiceImpl implements DiscountsService {
    @Autowired
    private DiscountDao discountsRepository;


    public DiscountEntity createDiscount() {
        return new DiscountEntity();
    }


    public DiscountEntity addNewDiscount(DiscountDTO discountDTO) {
        DiscountEntity newDiscount = createDiscount();
        newDiscount.setCode(discountDTO.getCode());
        newDiscount.setDescription(discountDTO.getDescription());
        newDiscount.setDiscountPercentage(discountDTO.getDiscountPercentage());
        newDiscount.setStartDate(discountDTO.getStartDate());
        newDiscount.setEndDate(discountDTO.getEndDate());
        discountsRepository.save(newDiscount);
        return newDiscount;
    }

    public void deleteDiscount(DiscountEntity discount) {
        discountsRepository.delete(discount);
    }
    public void deleteDiscountById(Integer discountID) {
        discountsRepository.deleteById(discountID);
    }

    public DiscountEntity findDiscountById(Integer discountID) throws EntityNotFoundException {
        Optional<DiscountEntity> optionalDiscount = discountsRepository.findById(discountID);
        if (optionalDiscount.isPresent()) {
            return optionalDiscount.get();
        } else throw new EntityNotFoundException();
    }
    public DiscountEntity findDiscountByCode(String discountCode) throws EntityNotFoundException {
        Optional<DiscountEntity> optionalDiscount = discountsRepository.findByCodeContainingIgnoreCase(discountCode);
        if (optionalDiscount.isPresent()) {
            return optionalDiscount.get();
        } else throw new EntityNotFoundException();
    }


    public void updateDiscountCode(Integer discountID, String newCode) {
        discountsRepository.updateDiscountCode(discountID, newCode);
    }
    public void updateDiscountDescription(Integer discountID, String newDescription) {
        discountsRepository.updateDiscountDescription(discountID, newDescription);
    }
    public void updateDiscountPercentage(Integer discountID, BigDecimal newPercentage) {
        discountsRepository.updateDiscountPercentage(discountID, newPercentage);
    }
    public void updateDiscountStartDate(Integer discountID, String newStartDate) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        discountsRepository.updateDiscountStartDate(discountID, LocalDateTime.parse(newStartDate, formatter));
    }
    public void updateDiscountEndDate(Integer discountID, String newEndDate) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        discountsRepository.updateDiscountEndDate(discountID, LocalDateTime.parse(newEndDate, formatter));
    }
    public void updateDiscount(Integer discountID, String newCode, String newDescription, BigDecimal newPercentage,
                               String newStartDate, String newEndDate) {
        discountsRepository.updateDiscount(discountID, newCode, newDescription, newPercentage,
                LocalDateTime.parse(newStartDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                LocalDateTime.parse(newEndDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
    }
}
