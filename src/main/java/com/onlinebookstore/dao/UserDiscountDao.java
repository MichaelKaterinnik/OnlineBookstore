package com.onlinebookstore.dao;

import com.onlinebookstore.domain.UserDiscountEntity;
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
public interface UserDiscountDao extends JpaRepository<UserDiscountEntity, Integer> {

    @Override
    List<UserDiscountEntity> findAll();

    Optional<UserDiscountEntity> findById(Integer id);
    Optional<UserDiscountEntity> findByUserId(Integer id);
    Optional<UserDiscountEntity> findByUser(UserEntity user);



    @Modifying
    @Query("UPDATE UserDiscountEntity ud SET ud.discountId = :discountId WHERE ud.id = :id")
    void updateDiscountId(@Param("id") Integer id, @Param("discountId") Integer discountId);


    @Override
    <S extends UserDiscountEntity> S save(S entity);

    @Override
    void delete(UserDiscountEntity entity);
    @Override
    void deleteById(Integer id);
}
