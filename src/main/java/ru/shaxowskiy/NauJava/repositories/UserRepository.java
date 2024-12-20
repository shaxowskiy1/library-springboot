package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shaxowskiy.NauJava.models.User;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "users")
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Поиск пользователя книги по юзернейму
     * @param username юзнернейм пользователя
     * @return пользователь
     */
    Optional<User> findUserByUsername(String username);

    /**
     * Поиск пользователя по коду активации
     * @param code код активации
     * @return пользователь
     */
    User findByActivationCode(String code);

}
