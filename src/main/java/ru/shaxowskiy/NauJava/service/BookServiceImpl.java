package ru.shaxowskiy.NauJava.service;

import ru.shaxowskiy.NauJava.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.repositories.BookRepository;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void createBook(Integer id, String title, Long isbn, int publicationYear, boolean isAvailable) {
        Book newBook = new Book();
        newBook.setId(id);
        newBook.setIsbn(isbn);
        newBook.setTitle(title);
        newBook.setPublicationYear(publicationYear);
        newBook.setAvailable(isAvailable);

        bookRepository.create(newBook);

    }


    @Override
    public Book findById(Integer id) {
        return bookRepository.read(id);
    }

    @Override
    public void deleteById(Integer id) {
        bookRepository.delete(id);
    }

    @Override
    public void updateBook(Integer id, String title, Long isbn, int publicationYear, boolean isAvailable) {
        Book newBook = new Book();

        newBook.setId(id);
        newBook.setTitle(title);
        newBook.setIsbn(isbn);
        newBook.setPublicationYear(publicationYear);
        newBook.setAvailable(isAvailable);

        bookRepository.update(newBook);

    }
}
