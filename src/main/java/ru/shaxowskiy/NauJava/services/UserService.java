package ru.shaxowskiy.NauJava.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.models.enums.Role;
import ru.shaxowskiy.NauJava.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * Поиск списка всех юзеров
     * @return список юзеров
     */
    public List<User> findAll() {
        log.info("Метод findAll в классе UserService: ");
        List<User> listOfUsers = new ArrayList<>();
        userRepository.findAll().forEach(listOfUsers::add);
        return listOfUsers;
    }

    public User findById(Long id) {
        log.info("Метод поиска юзера c id={} в методе UserService", id);
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    /**
     * Получение данных об аутентифицированном пользователе
     * @return объект юзера
     */
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetails) principal).getUsername();
        log.info("Username запрашивающий свой эндпоинт логина: {}", username);

        return findByUsername(username);

    }

    /**
     * Смена роли у юзера на переданную в метод
     * @param user юзер
     * @param role роль на которую поменять
     */
    public void assignRole(User user, Role role) {
        user.getRoles().clear();
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
