package com.onlinebookstore.services;

import com.onlinebookstore.dao.CollectionBookDao;
import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.CollectionBookEntity;
import com.onlinebookstore.domain.CollectionEntity;
import com.onlinebookstore.models.BookDTO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
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
    public void setCollectionForNewBook(Integer collectionId, Integer bookId) {
        CollectionBookEntity collectionBook = new CollectionBookEntity();
        collectionBook.setCollectionId(collectionId);
        collectionBook.setBookId(bookId);
        collectionBookRepository.save(collectionBook);
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
        } else
            throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        collectionBookRepository.deleteById(id);
    }



    public void definingNewBookGenres(BookDTO book, BookEntity newBook) {
        CollectionEntity newBookGenre;
        for (String genre : book.getGenres()) {
            CollectionEntity thisBookGenre = collectionsService.findCollectionByName(genre);
            if (thisBookGenre != null) {
                setCollectionForNewBook(thisBookGenre.getId(), newBook.getId());
                newBook.getCollections().add(thisBookGenre);
                thisBookGenre.getBooks().add(newBook);
                entityManager.merge(newBook);
                entityManager.merge(thisBookGenre);
            } else {
                newBookGenre = collectionsService.addNewCollectionForNewBook(genre);
                setCollectionForNewBook(newBookGenre.getId(), newBook.getId());
                newBook.getCollections().add(newBookGenre);
                newBookGenre.getBooks().add(newBook);
                entityManager.merge(newBook);
                entityManager.merge(newBookGenre);
            }
        }
    }

}
