package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.shaxowskiy.NauJava.models.Category;
import ru.shaxowskiy.NauJava.models.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
}
