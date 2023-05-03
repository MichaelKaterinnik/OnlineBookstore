package com.onlinebookstore.controllers;

import com.onlinebookstore.config.GlobalExceptionHandler;
import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.CollectionDTO;
import com.onlinebookstore.services.CollectionBooksService;
import com.onlinebookstore.services.CollectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * У даному класи реалізовані шляхи запитів на:
 * - отримання списку зареєстрованих на сайті колекцій ("/collection/get_all"), цей  метод доступний для всіх користувачів
 * <p>
 * Усі інші методи доступні лише для адміністратора, вони пов'язані з додаванням/редагуванням інфомрації про колекції, додаванням/видаленням
 * книги з певної колекції, видаленням колекції.
 */

@RestController
@RequestMapping("/collection")
public class CollectionsController {
    @Autowired
    private CollectionsService collectionsService;
    @Autowired
    private CollectionBooksService collectionBooksService;
    @Autowired
    private GlobalExceptionHandler exceptionHandler;


    // GUEST, USER, ADMIN
    @GetMapping("/get_all")
    public ResponseEntity<List<CollectionDTO>> getBookCollections(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "30") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<CollectionDTO> collectionsList = collectionsService.getAllCollectionsDTO(pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(collectionsList, headers, HttpStatus.OK);
    }


    // ADMIN
    @PostMapping("/add")
    public ResponseEntity<CollectionEntity> addCollection(@RequestBody CollectionDTO collectionDTO) {
        CollectionEntity createdCollection = collectionsService.addNewCollection(collectionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCollection);
    }

    // ADMIN
    @PostMapping("/books/add")
    public ResponseEntity<String> addBookToCollection(@RequestBody Integer collectionID,
                                                      @RequestBody Integer bookID) {
        try {
            collectionBooksService.addBookInCollection(collectionID, bookID);
        } catch (RuntimeException e) {
            exceptionHandler.runtimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Книгу додано до колекції!");
    }
    @DeleteMapping("/books/delete")
    public ResponseEntity<Object> deleteCollectionById(@RequestBody Integer collectionID,
                                                       @RequestBody Integer bookID) {
        try {
            collectionBooksService.removeBookFromCollection(collectionID, bookID);
        } catch (RuntimeException e) {
            exceptionHandler.runtimeException(e);
        }
        return ResponseEntity.ok().body("Книгу видалено з колекції!");
    }

    // ADMIN
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCollectionById(@PathVariable Integer collectionID) {
        collectionsService.deleteCollectionById(collectionID);
        return ResponseEntity.ok().body("Категорія книг видалена з бази.");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteСollection(@PathVariable CollectionEntity collection) {
        collectionsService.deleteCollection(collection);
        return ResponseEntity.ok().body("Категорія книг була успішно видалена з бази.");
    }

    // ADMIN
    @PutMapping("/update")
    public ResponseEntity<Object> updateCollection(@RequestBody CollectionDTO collectionDTO, @PathVariable Integer collectionID) {
        collectionsService.updateCollection(collectionID, collectionDTO.getName(), collectionDTO.getDescription());
        return ResponseEntity.ok().body("Інформацію про категорію книг оновлено!");
    }
    @PutMapping("/update/name")
    public ResponseEntity<Object> updateCollectionName(@RequestBody CollectionDTO collectionDTO, @PathVariable Integer collectionID) {
        collectionsService.updateCollectionName(collectionID, collectionDTO.getName());
        return ResponseEntity.ok().body("Інформацію про категорію книг оновлено!");
    }
    @PutMapping("/update/description")
    public ResponseEntity<Object> updateCollectionDescription(@RequestBody CollectionDTO collectionDTO, @PathVariable Integer collectionID) {
        collectionsService.updateCollectionDescription(collectionID, collectionDTO.getDescription());
        return ResponseEntity.ok().body("Інформацію про категорію книг оновлено!");
    }
}
