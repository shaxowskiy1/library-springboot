package ru.shaxowskiy.NauJava.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Поиск всех книг
     * @return список книг
     */
    public List<Book> findAll(){
        List<Book> bookList = new ArrayList<>();
        bookRepository.findAll().forEach(bookList::add);
        return bookList;
    }
}
