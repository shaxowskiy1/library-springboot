package ru.shaxowskiy.NauJava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.repositories.BookRepository;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/findByAuthorOrTitle")
    public List<Book> findByAuthorOrTitle(@RequestParam("author") String author,
                                          @RequestParam("title") String title){
        return bookRepository.findBooksByAuthorOrTitle(author, title);

    }


    @GetMapping("/findBooksByCategory")
    public List<Book> findBooksByCategory(@RequestParam("title")String title){
        return bookRepository.findBooksByCategory(title);
    }
}
