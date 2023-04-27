package com.onlinebookstore.dao;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface OrderItemDao extends JpaRepository<OrderItemEntity, Integer> {

    List<OrderItemEntity> findAllByOrderId(Integer id);

    Optional<OrderItemEntity> findById(Integer id);

    OrderItemEntity getById(Integer id);


    @Query("SELECT oi.book FROM OrderItemEntity oi WHERE oi.order.id = :orderId")
    List<BookEntity> findBooksByOrderId(@Param("orderId") Integer orderId);

    @Override
    <S extends OrderItemEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);

    @Override
    void delete(OrderItemEntity entity);
}
