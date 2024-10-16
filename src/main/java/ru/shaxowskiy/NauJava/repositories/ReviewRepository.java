package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shaxowskiy.NauJava.models.Review;

@RepositoryRestResource(path = "reviews")
public interface ReviewRepository extends CrudRepository<Review, Long> {
}
