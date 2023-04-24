package com.onlinebookstore.dao;

import com.onlinebookstore.domain.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface AuthorDao extends JpaRepository<AuthorEntity, Integer> {

    List<AuthorEntity> findAll();

    List<AuthorEntity> findAllByLastNameContainingIgnoreCase(String lastName);

    Optional<AuthorEntity> findById(Integer id);

    Optional<AuthorEntity> findByLastName(String lastName);


    @Override
    <S extends AuthorEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);
}
