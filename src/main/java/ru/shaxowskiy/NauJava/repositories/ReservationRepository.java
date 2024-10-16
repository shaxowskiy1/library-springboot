package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shaxowskiy.NauJava.models.Reservation;

@RepositoryRestResource(path = "reservations")
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
}
