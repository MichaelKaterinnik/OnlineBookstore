package com.onlinebookstore.dao;

import com.onlinebookstore.domain.CollectionBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Repository
public interface CollectionBookDao extends JpaRepository<CollectionBookEntity, Integer> {

    @Override
    Optional<CollectionBookEntity> findById(Integer integer);

    @Override
    <S extends CollectionBookEntity> S save(S entity);

    @Modifying
    @Transactional
    @Query("DELETE FROM CollectionBookEntity c WHERE c.collection.id = :genreCollectionId AND c.book.id = :bookId")
    void removeBookFromGenreCollection(@Param("genreCollectionId") Integer genreCollectionId, @Param("bookId") Integer bookId);

    @Override
    void deleteById(Integer integer);
}
