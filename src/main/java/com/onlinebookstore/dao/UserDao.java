package com.onlinebookstore.dao;

import com.onlinebookstore.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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




    @Override
    <S extends UserEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);

    void deleteByEmail(String email);
}
