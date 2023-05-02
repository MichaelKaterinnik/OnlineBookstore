package com.onlinebookstore.dao;

import com.onlinebookstore.domain.OrderEntity;
import com.onlinebookstore.domain.UserEntity;
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
public interface OrderDao extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findAll();

    List<OrderEntity> findAllByUserId(Integer id);
    List<OrderEntity> findAllByUser(UserEntity user);

    Optional<OrderEntity> findById(Integer id);

    @Override
    <S extends OrderEntity> S save(S entity);

    @Modifying
    @Query("UPDATE OrderEntity o SET o.status = :status WHERE o.id = :id")
    void updateOrderStatus(@Param("id") Integer id, @Param("status") OrderEntity.OrderStatus status);


    @Override
    void deleteById(Integer integer);

    @Override
    void delete(OrderEntity entity);
}
