package com.onlinebookstore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "discounts", schema = "online_bookstore")
public class DiscountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "code")
    @Pattern(regexp = "^[a-zA-Z0-9]{8}$", message = "Код повинен складатись з 8 символів (латиниця, числові символи)")
    private String code;
    @Basic
    @Column(name = "description")
    @Pattern(regexp = "^[\\p{L}0-9\\s.,;:!?'\"()\\[\\]{}«»-]+$", message = "Опис знижки має бути українською мовою")
    @Size(max = 255, message = "Опис повинен містити не більше 255 символів")
    private String description;
    @Basic
    @Column(name = "discount_percentage")
    @DecimalMin(value = "1", message = "Відсоток знижки не може бути менше 1%")
    @DecimalMax(value = "100", message = "Відсоток знижки не може бути більше 100%")
    private BigDecimal discountPercentage;
    @Basic
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}$", message = "Дата початку дії знижки має бути зазначена у форматі dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDate;
    @Basic
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}$", message = "Дата кінця дії знижки має бути зазначена у форматі dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountEntity that = (DiscountEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(code, that.code) && Objects.equals(description, that.description) && Objects.equals(discountPercentage, that.discountPercentage) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, discountPercentage, startDate, endDate);
    }
}
