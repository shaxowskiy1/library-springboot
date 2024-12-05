package ru.shaxowskiy.NauJava.services;

import jakarta.persistence.EntityNotFoundException;
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

    /**
     * Поиск всех книги по id
     * @param id айди книги
     * @return книга с заданным айди
     */
    public Book findById(Long id){
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    /**
     * Сохранение книги в базу данных
     * @param book книга
     */
    public void addBook(Book book) {
        bookRepository.save(book);
    }
}
