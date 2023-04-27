package com.onlinebookstore.services;

import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.CollectionDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface CollectionsService {
    CollectionEntity createCollection();

    void addNewCollection(CollectionDTO collectionDTO);
    CollectionEntity addNewCollectionForNewBook(String collectionName);

    void updateCollectionDescription(Integer id, String description);
    void updateCollectionName(Integer id, String newName);
    void updateCollection(Integer id, String newName, String newDescription);

    void deleteCollection(CollectionEntity collection);
    void deleteCollectionById(Integer id);

    List<CollectionEntity> getAllCollections();
    CollectionEntity findCollectionByName(String name);
    CollectionEntity findCollectionById(Integer id);
}
