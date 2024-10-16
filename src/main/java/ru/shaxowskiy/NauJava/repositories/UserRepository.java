package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shaxowskiy.NauJava.models.User;

@RepositoryRestResource(path = "users")
public interface UserRepository extends CrudRepository<User, Long> {
}
