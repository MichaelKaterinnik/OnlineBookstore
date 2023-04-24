package com.onlinebookstore.dao;

import com.onlinebookstore.domain.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
@Repository
public interface WishlistDao extends JpaRepository<WishlistEntity, Integer> {
    @Override
    Optional<WishlistEntity> findById(Integer integer);

    Optional<WishlistEntity> findByUserId(Integer userID);


    @Override
    <S extends WishlistEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);
}
