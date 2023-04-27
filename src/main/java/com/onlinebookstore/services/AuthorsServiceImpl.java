package com.onlinebookstore.services;

import com.onlinebookstore.dao.AuthorDao;
import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.models.AuthorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorsServiceImpl implements AuthorsService {
    @Autowired
    private AuthorDao authorsRepository;


    public AuthorEntity createAuthor() {
        return new AuthorEntity();
    }


    public void addNewAuthor(AuthorDTO author) {
        AuthorEntity newAuthor = createAuthor();
        newAuthor.setFirstName(author.getFirstName());
        newAuthor.setLastName(author.getLastName());
        newAuthor.setBio(author.getBio());
        authorsRepository.save(newAuthor);
    }
    public void addNewAuthorForNewBook(String firstName, String lastName) {
        AuthorEntity newAuthor = createAuthor();
        newAuthor.setFirstName(firstName);
        newAuthor.setLastName(lastName);
        authorsRepository.save(newAuthor);
    }

    public void updateAuthorFirstAndLastName(Integer authorID, String firstName, String lastName) {
        authorsRepository.updateAuthorFirstAndLastName(authorID, firstName, lastName);
    }
    public void updateAuthorBio(Integer authorID, String bio) {
        authorsRepository.updateAuthorBio(authorID, bio);
    }
    public void updateAuthor(Integer authorID, String firstName, String lastName, String bio) {
        authorsRepository.updateAuthor(authorID, firstName, lastName, bio);
    }

    public void deleteAuthor(AuthorEntity author) {
        authorsRepository.delete(author);
    }
    public void deleteAuthorById(Integer id) {
        authorsRepository.deleteById(id);
    }

    public List<AuthorEntity> getAllAuthors() {
        return authorsRepository.findAll();
    }
    public ArrayList<AuthorEntity> findAllOrderByLastName() {
        return (ArrayList<AuthorEntity>) authorsRepository.findAllOrderByLastNameAsc();
    }
    public List<AuthorEntity> findAllAuthorsByLastName(String lastName) throws IllegalArgumentException {
        if (lastName.length() < 2) {
            throw new IllegalArgumentException();
        }
        return authorsRepository.findAllByLastNameContainingIgnoreCase(lastName);
    }
    public AuthorEntity findAuthorsByFirstAndLastName(String firstName, String lastName) throws EntityNotFoundException {
        Optional<AuthorEntity> optionalAuthor = authorsRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
        if (optionalAuthor.isPresent()) {
            return optionalAuthor.get();
        } else throw new EntityNotFoundException();
    }
    public AuthorEntity findAuthorById(Integer authorID) throws EntityNotFoundException {
        Optional<AuthorEntity> optionalAuthor = authorsRepository.findById(authorID);
        if (optionalAuthor.isPresent()) {
            return optionalAuthor.get();
        } else throw new EntityNotFoundException();
    }

}
