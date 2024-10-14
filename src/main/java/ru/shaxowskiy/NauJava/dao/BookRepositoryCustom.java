package ru.shaxowskiy.NauJava.dao;

import org.springframework.stereotype.Repository;
import ru.shaxowskiy.NauJava.models.Book;

import java.util.List;

@Repository
public interface BookRepositoryCustom {
    /**
     * Нахождение книг по автору или по названию
     * @param author автор книги
     * @param title название книги
     * @return
     */
    List<Book> findBooksByAuthorOrTitle(String author, String title);

    /**
     * Нахождение списка книг по категории
     * @param title название категории
     * @return
     */
    List<Book> findBooksByCategory(String title);
}
