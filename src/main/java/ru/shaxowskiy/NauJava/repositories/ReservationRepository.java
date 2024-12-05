package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Reservation;
import ru.shaxowskiy.NauJava.models.User;

import java.util.List;


@RepositoryRestResource(path = "reservations")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    /**
     * Поиск по книге и пользователю, который зарезерировал
     * @param book книга
     * @param user пользователь
     * @return
     */
    Reservation findByBookIdAndUserId(Book book, User user);

    /**
     * поиск всех книг по айди книги
     * @param book книга
     * @return
     */
    List<Reservation> findAllByBookId(Book book);

    /**
     * Поиск резерваций у юзера
     * @param user юзер
     * @return список резерваций
     */
    List<Reservation> findByUserId(User user);
}
