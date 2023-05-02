package com.onlinebookstore.services;

import com.onlinebookstore.dao.DiscountDao;
import com.onlinebookstore.domain.DiscountEntity;
import com.onlinebookstore.models.DiscountDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class DiscountsServiceImpl implements DiscountsService {
    @Autowired
    private DiscountDao discountsRepository;


    public DiscountEntity createDiscount() {
        return new DiscountEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public DiscountEntity addNewDiscount(DiscountDTO discountDTO) {
        DiscountEntity newDiscount = createDiscount();
        newDiscount.setCode(discountDTO.getCode());
        newDiscount.setDescription(discountDTO.getDescription());
        newDiscount.setDiscountPercentage(discountDTO.getDiscountPercentage());
        newDiscount.setStartDate(LocalDateTime.parse(discountDTO.getStartDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        newDiscount.setEndDate(LocalDateTime.parse(discountDTO.getEndDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        discountsRepository.save(newDiscount);
        return newDiscount;
    }

    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteDiscount(DiscountEntity discount) {
        discountsRepository.delete(discount);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteDiscountById(Integer discountID) {
        discountsRepository.deleteById(discountID);
    }


    // get-methods:
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
    public List<DiscountEntity> findAllOrderedByExpiredDateDesc(Pageable pageable) {
        return discountsRepository.findAllOrderedByExpiredDateDesc();
    }
    public List<DiscountEntity> findAllExpiredDiscounts(Pageable pageable) {
        return discountsRepository.findExpiredDiscountsOrderedByExpiredDateDesc();
    }
    public List<DiscountEntity> findAllNonExpiredDiscounts(Pageable pageable) {
        return discountsRepository.findActiveDiscountsOrderedByExpiredDateDesc();
    }


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateDiscountCode(Integer discountID, String newCode) {
        discountsRepository.updateDiscountCode(discountID, newCode);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateDiscountDescription(Integer discountID, String newDescription) {
        discountsRepository.updateDiscountDescription(discountID, newDescription);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateDiscountPercentage(Integer discountID, BigDecimal newPercentage) {
        discountsRepository.updateDiscountPercentage(discountID, newPercentage);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateDiscountStartDate(Integer discountID, String newStartDate) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        discountsRepository.updateDiscountStartDate(discountID, LocalDateTime.parse(newStartDate, formatter));
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateDiscountEndDate(Integer discountID, String newEndDate) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        discountsRepository.updateDiscountEndDate(discountID, LocalDateTime.parse(newEndDate, formatter));
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateDiscount(Integer discountID, String newCode, String newDescription, BigDecimal newPercentage,
                               String newStartDate, String newEndDate) {
        discountsRepository.updateDiscount(discountID, newCode, newDescription, newPercentage,
                LocalDateTime.parse(newStartDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                LocalDateTime.parse(newEndDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
    }
}
