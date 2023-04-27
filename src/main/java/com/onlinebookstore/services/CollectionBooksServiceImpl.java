package com.onlinebookstore.services;

import com.onlinebookstore.dao.CollectionBookDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.CollectionBookEntity;
import com.onlinebookstore.domain.CollectionEntity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class CollectionBooksServiceImpl implements CollectionBooksService {
    @Autowired
    private CollectionBookDao collectionBookRepository;
    @Autowired
    private CollectionsServiceImpl collectionsService;
    @Autowired
    private BooksService booksService;
    @Autowired
    private EntityManager entityManager;


    public CollectionBookEntity createCollectionBook() {
        return new CollectionBookEntity();
    }


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

    @Transactional
    public CollectionEntity setCollectionForNewBook(Integer collectionId, Integer bookId) {
        CollectionBookEntity collectionBook = new CollectionBookEntity();
        collectionBook.setCollectionId(collectionId);
        collectionBook.setBookId(bookId);
        collectionBookRepository.save(collectionBook);
        return collectionsService.findCollectionById(collectionId);
    }


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

}
