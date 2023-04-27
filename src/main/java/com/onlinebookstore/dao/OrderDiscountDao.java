package com.onlinebookstore.dao;

import com.onlinebookstore.domain.OrderDiscountEntity;
import com.onlinebookstore.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface OrderDiscountDao extends JpaRepository<OrderDiscountEntity, Integer> {

    @Override
    List<OrderDiscountEntity> findAll();

    Optional<OrderDiscountEntity> findById(Integer id);
    Optional<OrderDiscountEntity> findByOrderId(Integer id);
    Optional<OrderDiscountEntity> findByOrder(OrderEntity order);


    @Modifying
    @Query("UPDATE OrderDiscountEntity od SET od.discountId = :discountId WHERE od.id = :id")
    void updateDiscountId(@Param("id") Integer id, @Param("discountId") Integer discountId);


    @Override
    <S extends OrderDiscountEntity> S save(S entity);

    @Override
    void delete(OrderDiscountEntity entity);
    @Override
    void deleteById(Integer id);
}
