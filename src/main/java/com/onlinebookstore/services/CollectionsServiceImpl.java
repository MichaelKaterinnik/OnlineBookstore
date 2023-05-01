package com.onlinebookstore.services;

import com.onlinebookstore.dao.CollectionDao;
import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.CollectionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Service
public class CollectionsServiceImpl implements CollectionsService {
    @Autowired
    private CollectionDao collectionsRepository;


    public CollectionEntity createCollection() {
        return new CollectionEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CollectionEntity addNewCollection(CollectionDTO collectionDTO) {
        CollectionEntity newCollection = createCollection();
        newCollection.setName(collectionDTO.getName());
        newCollection.setDescription(collectionDTO.getDescription());
        collectionsRepository.save(newCollection);
        return newCollection;
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CollectionEntity addNewCollectionForNewBook(String collectionName) {
        CollectionEntity newCollection = createCollection();
        newCollection.setName(collectionName);
        collectionsRepository.save(newCollection);
        return newCollection;
    }


    // update-methods:
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateCollectionDescription(Integer id, String description) {
        collectionsRepository.updateCollectionDescription(id, description);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateCollectionName(Integer id, String newName) {
        collectionsRepository.updateCollectionName(id, newName);
    }
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateCollection(Integer id, String newName, String newDescription) {
        collectionsRepository.updateCollection(id, newName, newDescription);
    }

    // delete-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteCollection(CollectionEntity collection) {
        collectionsRepository.delete(collection);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteCollectionById(Integer id) {
        collectionsRepository.deleteById(id);
    }


    // get-methods:
    public List<CollectionEntity> getAllCollections(Pageable pageable) {
        return collectionsRepository.findAll();
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
