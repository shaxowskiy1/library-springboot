package ru.shaxowskiy.NauJava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shaxowskiy.NauJava.repositories.BookRepository;

@Controller
@RequestMapping("/books/view")
public class BookControllerView {

    private BookRepository bookRepository;

    @Autowired
    public BookControllerView(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/list")
    public String booksListView(Model model){
        model.addAttribute("books", bookRepository.findAll());
        return "booksList";
    }


}
