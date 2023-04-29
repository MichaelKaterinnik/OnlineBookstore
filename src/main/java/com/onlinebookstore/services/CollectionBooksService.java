package com.onlinebookstore.services;

import com.onlinebookstore.domain.CollectionBookEntity;
import com.onlinebookstore.domain.CollectionEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface CollectionBooksService {
    public CollectionBookEntity createCollectionBook();

    void addBookInCollection(Integer collectionId, Integer bookId);

    void removeBookFromCollection(Integer collectionID, Integer bookID);

    CollectionEntity setCollectionForNewBook(Integer collectionId, Integer bookId);

    void deleteById(Integer integer);
}
