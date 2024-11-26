package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Reservation;
import ru.shaxowskiy.NauJava.models.User;

import java.util.List;


@RepositoryRestResource(path = "reservations")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByBookIdAndUserId(Book book, User user);

    List<Reservation> findAllByBookId(Book book);
}
