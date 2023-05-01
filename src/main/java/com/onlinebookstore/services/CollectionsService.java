package com.onlinebookstore.services;

import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.CollectionDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CollectionsService {
    CollectionEntity createCollection();

    CollectionEntity addNewCollection(CollectionDTO collectionDTO);
    CollectionEntity addNewCollectionForNewBook(String collectionName);

    void updateCollectionDescription(Integer id, String description);
    void updateCollectionName(Integer id, String newName);
    void updateCollection(Integer id, String newName, String newDescription);

    void deleteCollection(CollectionEntity collection);
    void deleteCollectionById(Integer id);

    List<CollectionEntity> getAllCollections(Pageable pageable);
    List<CollectionEntity> getAllCollections();
    CollectionEntity findCollectionByName(String name);
    CollectionEntity findCollectionById(Integer id);
}
