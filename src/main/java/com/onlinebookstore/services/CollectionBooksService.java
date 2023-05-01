package com.onlinebookstore.services;

import com.onlinebookstore.domain.BookEntity;
import com.onlinebookstore.domain.CollectionBookEntity;
import com.onlinebookstore.models.BookDTO;

public interface CollectionBooksService {
    public CollectionBookEntity createCollectionBook();

    void addBookInCollection(Integer collectionId, Integer bookId);

    void removeBookFromCollection(Integer collectionID, Integer bookID);

    void setCollectionForNewBook(Integer collectionId, Integer bookId);

    void deleteById(Integer integer);


    void definingNewBookGenres(BookDTO book, BookEntity newBook);
}
