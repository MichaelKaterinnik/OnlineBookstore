package com.onlinebookstore.services;

import com.onlinebookstore.dao.CollectionDao;
import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.CollectionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class CollectionsServiceImpl implements CollectionsService {
    @Autowired
    private CollectionDao collectionsRepository;


    public CollectionEntity createCollection() {
        return new CollectionEntity();
    }


    public void addNewCollection(CollectionDTO collectionDTO) {
        CollectionEntity newCollection = createCollection();
        newCollection.setName(collectionDTO.getName());
        newCollection.setDescription(collectionDTO.getDescription());
        collectionsRepository.save(newCollection);
    }
    public CollectionEntity addNewCollectionForNewBook(String collectionName) {
        CollectionEntity newCollection = createCollection();
        newCollection.setName(collectionName);
        collectionsRepository.save(newCollection);
        return newCollection;
    }


    public void updateCollectionDescription(Integer id, String description) {
        collectionsRepository.updateCollectionDescription(id, description);
    }
    public void updateCollectionName(Integer id, String newName) {
        collectionsRepository.updateCollectionName(id, newName);
    }
    public void updateCollection(Integer id, String newName, String newDescription) {
        collectionsRepository.updateCollection(id, newName, newDescription);
    }


    public void deleteCollection(CollectionEntity collection) {
        collectionsRepository.delete(collection);
    }
    public void deleteCollectionById(Integer id) {
        collectionsRepository.deleteById(id);
    }


    public List<CollectionEntity> getAllCollections() {
        return collectionsRepository.findAll();
    }
    public CollectionEntity findCollectionByName(String name) throws EntityNotFoundException {
        Optional<CollectionEntity> optionalCollection = collectionsRepository.findByNameContainingIgnoreCase(name);
        if (optionalCollection.isPresent()) {
            return optionalCollection.get();
        } else throw new EntityNotFoundException();
    }
    public CollectionEntity findCollectionById(Integer id) throws EntityNotFoundException {
        Optional<CollectionEntity> optionalCollection = collectionsRepository.findById(id);
        if (optionalCollection.isPresent()) {
            return optionalCollection.get();
        } else throw new EntityNotFoundException();
    }

}
