package com.onlinebookstore.services;

import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.models.AuthorDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
public interface AuthorsService {
    AuthorEntity createAuthor();

    void addNewAuthor(AuthorDTO author);
    void addNewAuthorForNewBook(String firstName, String lastName);

    void updateAuthorFirstAndLastName(Integer authorID, String firstName, String lastName);
    void updateAuthorBio(Integer authorID, String bio);
    void updateAuthor(Integer authorID, String firstName, String lastName, String bio);

    void deleteAuthor(AuthorEntity author);
    void deleteAuthorById(Integer id);

    List<AuthorEntity> getAllAuthors();
    public ArrayList<AuthorEntity> findAllOrderByLastName();
    List<AuthorEntity> findAllAuthorsByLastName(String lastName);
    AuthorEntity findAuthorsByFirstAndLastName(String firstName, String lastName);
    AuthorEntity findAuthorById(Integer authorID);
}
