package com.onlinebookstore.dao;

import com.onlinebookstore.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface OrderDao extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findAll();

    List<OrderEntity> findAllByUserId(Integer id);

    Optional<OrderEntity> findById(Integer id);


    @Override
    <S extends OrderEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);

    @Override
    void delete(OrderEntity entity);
}
