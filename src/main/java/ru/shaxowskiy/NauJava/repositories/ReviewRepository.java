package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.shaxowskiy.NauJava.models.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
