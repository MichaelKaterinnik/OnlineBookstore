package com.onlinebookstore.dao;

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
public interface UserDao extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findAll();

    Optional<UserEntity> findById(Integer id);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByPhone(String phone);

    List<UserEntity> findAllByPassword(String password);


    @Modifying
    @Query("UPDATE UserEntity u SET u.firstName = :firstName, u.lastName = :lastName, u.phone = :phone, u.email = :email, u.password = :password WHERE u.id = :id")
    void updateUserInfo(@Param("id") Integer id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("phone") String phone, @Param("email") String email, @Param("password") String password);

    @Modifying
    @Query("UPDATE UserEntity u SET u.firstName = :firstName, u.lastName = :lastName WHERE u.id = :id")
    void updateUserFirstAndLastName(@Param("id") Integer id, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Modifying
    @Query("UPDATE UserEntity u SET u.phone = :phone WHERE u.id = :id")
    void updateUserPhone(@Param("id") Integer id, @Param("phone") String phone);

    @Modifying
    @Query("UPDATE UserEntity u SET u.email = :email WHERE u.id = :id")
    void updateUserEmail(@Param("id") Integer id, @Param("email") String email);

    @Modifying
    @Query("UPDATE UserEntity u SET u.password = :password WHERE u.id = :id")
    void updateUserPassword(@Param("id") Integer id, @Param("password") String password);


    @Override
    <S extends UserEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);

    void deleteByEmail(String email);
}
