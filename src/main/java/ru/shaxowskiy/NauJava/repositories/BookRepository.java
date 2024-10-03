package ru.shaxowskiy.NauJava.repositories;

import ru.shaxowskiy.NauJava.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookRepository implements CrudRepository<Book, Integer> {

    private final List<Book> bookContainer;

    @Autowired
    public BookRepository(List<Book> userContainer) {
        this.bookContainer = userContainer;
    }

    @Override
    public void create(Book entity) {
        bookContainer.add(entity);
    }


    @Override
    public Book read(Integer id) {
        return bookContainer.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Book entity) {
        bookContainer.set(entity.getId(), entity);
    }

    @Override
    public void delete(Integer id) {
        bookContainer.removeIf(book -> book.getId() == id);
    }

}
