package com.onlinebookstore.controllers;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.services.BooksService;
import com.onlinebookstore.services.CollectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class MainPageController {
    @Autowired
    private BooksService booksService;
    @Autowired
    private CollectionsService collectionsService;


    @GetMapping(value = "/")
    @ResponseBody
    public Map<String, Object> getHome(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<BookEntity> books = booksService.findPopularBooks(pageable);
        List<CollectionEntity> collections = collectionsService.getAllCollections();
        List<String> collectionNames = collections.stream()
                .map(CollectionEntity::getName)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("categories", collectionNames);

        return response;
    }

}
