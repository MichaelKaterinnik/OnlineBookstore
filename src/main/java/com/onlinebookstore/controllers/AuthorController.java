package com.onlinebookstore.controllers;

import com.onlinebookstore.config.GlobalExceptionHandler;
import com.onlinebookstore.domain.AuthorEntity;
import com.onlinebookstore.models.AuthorDTO;
import com.onlinebookstore.models.BookDTO;
import com.onlinebookstore.services.AuthorsService;
import com.onlinebookstore.services.BooksService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    @Autowired
    private AuthorsService authorsService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private GlobalExceptionHandler exceptionHandler;


    // GUEST, USER, ADMIN
    @GetMapping("/get_all")
    public ResponseEntity<List<AuthorDTO>> getAuthors(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "30") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<AuthorDTO> authorsList = authorsService.getAllAuthorsDTO(pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(authorsList, headers, HttpStatus.OK);
    }

    // GUEST, USER, ADMIN
    @GetMapping("/get/{name}")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByLastName(@PathVariable String authorName,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "30") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<AuthorDTO> authorsList = authorsService.getAllAuthorsDTOByLastName(authorName, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(authorsList, headers, HttpStatus.OK);
    }

    // GUEST, USER, ADMIN
    @GetMapping("/books_of_author")
    public ResponseEntity<List<BookDTO>> getBooksByAuthorId(Integer authorID, @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "30") int size) {
        AuthorEntity author = null;
        try {
            author = authorsService.findAuthorById(authorID);
        } catch (EntityNotFoundException e) {
            exceptionHandler.entityNotFoundException(e);
        }

        Pageable pageable = PageRequest.of(page, size);
        List<BookDTO> resultBooks = booksService.getBooksDTOByAuthor(author, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(resultBooks, headers, HttpStatus.OK);
    }

    // ADMIN
    @PostMapping("/add")
    public ResponseEntity<AuthorEntity> addAuthor(@RequestBody AuthorDTO authorDTO) {
        AuthorEntity createdAuthor = authorsService.addNewAuthor(authorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    // ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteAuthorById(@PathVariable Integer authorID) {
        authorsService.deleteAuthorById(authorID);
        return ResponseEntity.ok().body("Автор був успішно видалений з бази.");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAuthor(@PathVariable AuthorEntity author) {
        authorsService.deleteAuthor(author);
        return ResponseEntity.ok().body("Автор був успішно видалений з бази.");
    }

    // ADMIN
    @PutMapping("/update")
    public ResponseEntity<Object> updateAuthor(@RequestBody AuthorDTO authorDTO, @PathVariable Integer authorID) {
        authorsService.updateAuthor(authorID, authorDTO.getFirstName(), authorDTO.getLastName(), authorDTO.getBio());
        return ResponseEntity.ok().body("Інформацію оновлено!");
    }
    @PutMapping("/update/bio")
    public ResponseEntity<Object> updateAuthorBio(@RequestBody AuthorDTO authorDTO, @PathVariable Integer authorID) {
        authorsService.updateAuthorBio(authorID, authorDTO.getBio());
        return ResponseEntity.ok().body("Інформацію оновлено!");
    }
    @PutMapping("/update/names")
    public ResponseEntity<Object> updateAuthorName(@RequestBody AuthorDTO authorDTO, @PathVariable Integer authorID) {
        authorsService.updateAuthorFirstAndLastName(authorID, authorDTO.getFirstName(), authorDTO.getLastName());
        return ResponseEntity.ok().body("Інформацію оновлено!");
    }

}
