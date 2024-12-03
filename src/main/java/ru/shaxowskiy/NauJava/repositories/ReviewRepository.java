package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Review;
import ru.shaxowskiy.NauJava.models.User;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "reviews")
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * Поиск отзыва по айди книги
     * @param book Книга
     * @return список отзывов
     */
    Optional<List<Review>> findByBookIdOrderByCreatedAtDesc(Book book);

    Review findByUserIdAndBookId(User user, Book book);
}
