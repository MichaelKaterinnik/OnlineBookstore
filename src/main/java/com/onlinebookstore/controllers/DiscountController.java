package com.onlinebookstore.controllers;

import com.onlinebookstore.config.GlobalExceptionHandler;
import com.onlinebookstore.domain.DiscountEntity;
import com.onlinebookstore.models.DiscountDTO;
import com.onlinebookstore.services.DiscountsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Шляхи даного класу-контролеру доступні лише для адміністратора і полягають в роботі із знижками.
 * У додатку передбачено застосування 3 видів знижок:
 * - присвоєних конкретній книзі (застосовується автоматично при додаванні книги користувачем до замолвення)
 * - присвоєних коритсувачу (застосовується при підтвердженні замовлення на всі книги конкретноогьо користувача)
 * - присвоєних певному промокоду (застосовується при введенні користувачем промокоду під час формування замовлення)
 * <p>
 * Методи цього класу дозволяються адміністратору доавати/редагувати знижки, виділяти їх. реалізоване отримання списку всіх наявних знижок,
 * а також всіх знижок, строк дії яких вже вийшов або яких ще не вийшов.
 */

@RestController
@RequestMapping("/discounts")
public class DiscountController {
    @Autowired
    private DiscountsService discountsService;
    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    // ADMIN
    @GetMapping("/get/all")
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "40") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<DiscountDTO> discountList = discountsService.getAllOrderedByExpiredDateDescDTO(pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(discountList, headers, HttpStatus.OK);
    }
    @GetMapping("/get/all/non_expired")
    public ResponseEntity<List<DiscountDTO>> getAllAvailableDiscounts(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "40") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<DiscountDTO> discountList = discountsService.getAllNonExpiredDiscountsDTO(pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(discountList, headers, HttpStatus.OK);
    }
    @GetMapping("/get/all/expired")
    public ResponseEntity<List<DiscountDTO>> getAllExpiredDiscounts(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "40") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<DiscountDTO> discountList = discountsService.getAllExpiredDiscountsDTO(pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(discountList, headers, HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<DiscountDTO> getDiscountById(@PathVariable Integer discountID) {
        DiscountDTO discount = null;
        try {
            discount = discountsService.getDiscountDTOById(discountID);
        } catch (EntityNotFoundException e) {
            exceptionHandler.entityNotFoundException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(discount, headers, HttpStatus.OK);
    }

    // ADMIN
    @PostMapping("/add")
    public ResponseEntity<DiscountEntity> addDiscount(@RequestBody DiscountDTO discountDTO) {
        DiscountEntity createdDiscount = discountsService.addNewDiscount(discountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscount);
    }

    // ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteDiscountById(@PathVariable Integer discountID) {
        discountsService.deleteDiscountById(discountID);
        return ResponseEntity.ok().body("Знижка була успішно видалена з бази.");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteDiscount(@PathVariable DiscountEntity discount) {
        discountsService.deleteDiscount(discount);
        return ResponseEntity.ok().body("Знижка була успішно видалена з бази.");
    }

    // ADMIN
    @PutMapping("/update/all")
    public ResponseEntity<Object> fullDiscountUpdating(@RequestBody DiscountDTO discountDTO, @PathVariable Integer discountID) {
        discountsService.updateDiscount(discountDTO.getId(), discountDTO.getCode(), discountDTO.getDescription(), discountDTO.getDiscountPercentage(), discountDTO.getStartDate(), discountDTO.getEndDate());
        return ResponseEntity.ok().body("Інформацію про знижку оновлено!");
    }
    @PutMapping("/update/promocode")
    public ResponseEntity<Object> updateDiscountCode(@RequestBody String newPromo, @PathVariable Integer discountID) {
        discountsService.updateDiscountCode(discountID, newPromo);
        return ResponseEntity.ok().body("Інформацію про код знижки оновлено!");
    }
    @PutMapping("/update/description")
    public ResponseEntity<Object> updateDiscountDescription(@RequestBody String newDescription, @PathVariable Integer discountID) {
        discountsService.updateDiscountDescription(discountID, newDescription);
        return ResponseEntity.ok().body("Інформацію про опис знижки оновлено!");
    }
    @PutMapping("/update/percentage")
    public ResponseEntity<Object> updateDiscountPercentage(@RequestBody BigDecimal newPercentage, @PathVariable Integer discountID) {
        discountsService.updateDiscountPercentage(discountID, newPercentage);
        return ResponseEntity.ok().body("Інформацію про відсоток знижки оновлено!");
    }
    @PutMapping("/update/startdate")
    public ResponseEntity<Object> updateDiscountStartDate(@RequestBody String newStartDate, @PathVariable Integer discountID) {
        discountsService.updateDiscountStartDate(discountID, newStartDate);
        return ResponseEntity.ok().body("Інформацію про початок дії знижки оновлено!");
    }
    @PutMapping("/update/enddate")
    public ResponseEntity<Object> updateDiscountEndDate(@RequestBody String newEndDate, @PathVariable Integer discountID) {
        discountsService.updateDiscountEndDate(discountID, newEndDate);
        return ResponseEntity.ok().body("Інформацію про кінець дії знижки оновлено!");
    }

}
