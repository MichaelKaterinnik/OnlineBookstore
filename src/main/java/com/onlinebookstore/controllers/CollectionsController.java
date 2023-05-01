package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.CollectionDTO;
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

@RestController
@RequestMapping("/collection")
public class CollectionsController {
    @Autowired
    private CollectionsService collectionsService;


    // GUEST, USER, ADMIN
    @GetMapping("/get_all")
    public ResponseEntity<List<CollectionEntity>> getBookCollections(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "30") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<CollectionEntity> collectionsList = collectionsService.getAllCollections(pageable);

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
    @PutMapping("/update_name")
    public ResponseEntity<Object> updateCollectionName(@RequestBody CollectionDTO collectionDTO, @PathVariable Integer collectionID) {
        collectionsService.updateCollectionName(collectionID, collectionDTO.getName());
        return ResponseEntity.ok().body("Інформацію про категорію книг оновлено!");
    }
    @PutMapping("/update_description")
    public ResponseEntity<Object> updateCollectionDescription(@RequestBody CollectionDTO collectionDTO, @PathVariable Integer collectionID) {
        collectionsService.updateCollectionDescription(collectionID, collectionDTO.getDescription());
        return ResponseEntity.ok().body("Інформацію про категорію книг оновлено!");
    }
}
