package com.onlinebookstore.dao;

import com.onlinebookstore.domain.CategoryEntity;
import com.onlinebookstore.domain.GenreCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface CollectionDao extends JpaRepository<GenreCollectionEntity, Integer> {

    List<GenreCollectionEntity> findAll();

    GenreCollectionEntity getById(Integer id);

    Optional<CategoryEntity> getByName(String name);


    @Override
    <S extends GenreCollectionEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);

    @Override
    void delete(GenreCollectionEntity entity);
}
