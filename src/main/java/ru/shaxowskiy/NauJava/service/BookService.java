package ru.shaxowskiy.NauJava.service;

import ru.shaxowskiy.NauJava.models.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    void createBook(Integer id, String title, Long isbn, int publicationYear, boolean isAvailable);
    Book findById(Integer id);
    void deleteById(Integer id);
    void updateBook(Integer id, String title, Long isbn, int publicationYear, boolean isAvailable);
}
