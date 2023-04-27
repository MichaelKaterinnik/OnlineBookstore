package com.onlinebookstore.dao;

import com.onlinebookstore.domain.CollectionEntity;
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
public interface CollectionDao extends JpaRepository<CollectionEntity, Integer> {

    List<CollectionEntity> findAll();

    CollectionEntity getById(Integer id);

    Optional<CollectionEntity> findById(Integer id);

    Optional<CollectionEntity> findByNameContainingIgnoreCase(String name);



    @Modifying
    @Query("UPDATE CollectionEntity c SET c.name = :name, c.description = :description WHERE c.id = :id")
    void updateCollection(@Param("id") Integer id, @Param("name") String name, @Param("description") String description);

    @Modifying
    @Query("UPDATE CollectionEntity c SET c.description = :description WHERE c.id = :id")
    void updateCollectionDescription(@Param("id") Integer id, @Param("description") String description);

    @Modifying
    @Query("UPDATE CollectionEntity c SET c.name = :name WHERE c.id = :id")
    void updateCollectionName(@Param("id") Integer id, @Param("name") String name);



    @Override
    <S extends CollectionEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);

    @Override
    void delete(CollectionEntity entity);
}
