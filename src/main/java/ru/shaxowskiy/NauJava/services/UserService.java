package ru.shaxowskiy.NauJava.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username){
        return userRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
    public List<User> findAll(){
        List<User> listOfUsers = new ArrayList<>();
        userRepository.findAll().forEach(listOfUsers::add);
        return listOfUsers;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found                                     "));
    }
}
