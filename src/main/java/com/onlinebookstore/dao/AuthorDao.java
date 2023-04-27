package com.onlinebookstore.dao;

import com.onlinebookstore.domain.AuthorEntity;
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
public interface AuthorDao extends JpaRepository<AuthorEntity, Integer> {

    List<AuthorEntity> findAll();

    @Query("SELECT a FROM AuthorEntity a ORDER BY a.lastName ASC")
    List<AuthorEntity> findAllOrderByLastNameAsc();

    List<AuthorEntity> findAllByLastNameContainingIgnoreCase(String lastName);

    Optional<AuthorEntity> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);



    Optional<AuthorEntity> findById(Integer id);

    @Modifying
    @Query("UPDATE AuthorEntity a SET a.firstName = :firstName, a.lastName = :lastName, a.bio = :bio WHERE a.id = :id")
    void updateAuthor(@Param("id") Integer id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("bio") String bio);

    @Modifying
    @Query("UPDATE AuthorEntity a SET a.firstName = :firstName, a.lastName = :lastName WHERE a.id = :id")
    void updateAuthorFirstAndLastName(@Param("id") Integer id, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Modifying
    @Query("UPDATE AuthorEntity a SET a.bio = :bio WHERE a.id = :id")
    void updateAuthorBio(@Param("id") Integer id, @Param("bio") String bio);


    @Override
    <S extends AuthorEntity> S save(S entity);

    @Override
    void deleteById(Integer integer);
}
