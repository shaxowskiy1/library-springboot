package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shaxowskiy.NauJava.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findBooksByAuthorOrTitle(String author, String title);

    @Query("SELECT b FROM Book b JOIN Category c ON b.category.id = c.id WHERE c.title = :title")
    List<Book> findBooksByCategory(String title);
}
