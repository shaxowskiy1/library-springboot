package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.shaxowskiy.NauJava.models.Book;

import java.util.List;

@Repository
@RepositoryRestResource(path = "books")
public interface BookRepository extends CrudRepository<Book, Long> {
    /**
     * нахождение книг по атрибутам: автор или название
     * @param author автор книги
     * @param title название книги
     * @return
     */
    List<Book> findBooksByAuthorOrTitle(String author, String title);

    /**
     * Поиск книг по введённому значению названия категории
     * @param title название категории
     * @return
     */
    @Query("SELECT b FROM Book b JOIN Category c ON b.category.id = c.id WHERE c.title = :title")
    List<Book> findBooksByCategory(String title);
}
