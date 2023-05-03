package com.onlinebookstore.services;

import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.models.AuthorDTO;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface AuthorsService {
    AuthorEntity createAuthor();

    AuthorEntity addNewAuthor(AuthorDTO author);
    void addNewAuthorForNewBook(String firstName, String lastName);

    void updateAuthorFirstAndLastName(Integer authorID, String firstName, String lastName);
    void updateAuthorBio(Integer authorID, String bio);
    void updateAuthor(Integer authorID, String firstName, String lastName, String bio);

    void deleteAuthor(AuthorEntity author);
    void deleteAuthorById(Integer id);

    List<AuthorEntity> getAllAuthors(Pageable pageable);
    List<AuthorDTO> getAllAuthorsDTO(Pageable pageable);

    public ArrayList<AuthorEntity> findAllOrderByLastName();
    List<AuthorEntity> findAllAuthorsByLastName(String lastName, Pageable pageable);
    List<AuthorDTO> getAllAuthorsDTOByLastName(String lastName, Pageable pageable);

    AuthorEntity findAuthorsByFirstAndLastName(String firstName, String lastName);
    AuthorEntity findAuthorById(Integer authorID);
}
