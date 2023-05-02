package com.onlinebookstore.dao;

import com.onlinebookstore.domain.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface DiscountDao extends JpaRepository<DiscountEntity, Integer> {

    @Override
    List<DiscountEntity> findAll();

    @Query("SELECT d FROM DiscountEntity d ORDER BY d.endDate DESC")
    List<DiscountEntity> findAllOrderedByExpiredDateDesc();
    @Query("SELECT d FROM DiscountEntity d WHERE d.endDate < CURRENT_TIMESTAMP ORDER BY d.endDate DESC")
    List<DiscountEntity> findExpiredDiscountsOrderedByExpiredDateDesc();
    @Query("SELECT d FROM DiscountEntity d WHERE d.endDate >= CURRENT_TIMESTAMP ORDER BY d.endDate DESC")
    List<DiscountEntity> findActiveDiscountsOrderedByExpiredDateDesc();


    Optional<DiscountEntity> findById(Integer id);
    Optional<DiscountEntity> findByCodeContainingIgnoreCase(String code);




    @Modifying
    @Query("UPDATE DiscountEntity d SET d.code = :code WHERE d.id = :id")
    void updateDiscountCode(@Param("id") Integer id, @Param("code") String code);
    @Modifying
    @Query("UPDATE DiscountEntity d SET d.description = :description WHERE d.id = :id")
    void updateDiscountDescription(@Param("id") Integer id, @Param("description") String description);
    @Modifying
    @Query("UPDATE DiscountEntity d SET d.discountPercentage = :discountPercentage WHERE d.id = :id")
    void updateDiscountPercentage(@Param("id") Integer id, @Param("discountPercentage") BigDecimal discountPercentage);
    @Modifying
    @Query("UPDATE DiscountEntity d SET d.startDate = :startDate WHERE d.id = :id")
    void updateDiscountStartDate(@Param("id") Integer id, @Param("startDate") LocalDateTime startDate);
    @Modifying
    @Query("UPDATE DiscountEntity d SET d.endDate = :endDate WHERE d.id = :id")
    void updateDiscountEndDate(@Param("id") Integer id, @Param("endDate") LocalDateTime endDate);
    @Modifying
    @Query("UPDATE DiscountEntity d SET d.code = :code, d.description = :description, d.discountPercentage = :discountPercentage, d.startDate = :startDate, d.endDate = :endDate WHERE d.id = :id")
    void updateDiscount(@Param("id") Integer id, @Param("code") String code, @Param("description") String description, @Param("discountPercentage") BigDecimal discountPercentage, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Override
    <S extends DiscountEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);
    @Override
    void delete(DiscountEntity entity);
}
