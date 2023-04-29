package com.onlinebookstore.services;

import com.onlinebookstore.dao.CollectionBookDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.CollectionBookEntity;
import com.onlinebookstore.domain.CollectionEntity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class CollectionBooksServiceImpl implements CollectionBooksService {
    @Autowired
    private CollectionBookDao collectionBookRepository;
    @Autowired
    private CollectionsService collectionsService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private EntityManager entityManager;


    public CollectionBookEntity createCollectionBook() {
        return new CollectionBookEntity();
    }


    // add-methods:
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addBookInCollection(Integer collectionId, Integer bookId) {
        CollectionBookEntity collectionBook = new CollectionBookEntity();
        collectionBook.setCollectionId(collectionId);
        collectionBook.setBookId(bookId);

        booksService.findBookByID(bookId).getCollections().add(collectionsService.findCollectionById(collectionId));
        collectionsService.findCollectionById(collectionId).getBooks().add(booksService.findBookByID(bookId));
        entityManager.merge(booksService.findBookByID(bookId));
        entityManager.merge(collectionsService.findCollectionById(collectionId));

        collectionBookRepository.save(collectionBook);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CollectionEntity setCollectionForNewBook(Integer collectionId, Integer bookId) {
        CollectionBookEntity collectionBook = new CollectionBookEntity();
        collectionBook.setCollectionId(collectionId);
        collectionBook.setBookId(bookId);
        collectionBookRepository.save(collectionBook);
        return collectionsService.findCollectionById(collectionId);
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void removeBookFromCollection(Integer collectionID, Integer bookID) {
        CollectionEntity collection = collectionsService.findCollectionById(collectionID);
        BookEntity book = booksService.findBookByID(bookID);

        if (collection != null && book != null) {
            collection.getBooks().remove(book);
            book.getCollections().remove(collection);

            entityManager.merge(collection);
            entityManager.merge(book);

            collectionBookRepository.removeBookFromGenreCollection(collectionID, bookID);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        collectionBookRepository.deleteById(id);
    }

}
