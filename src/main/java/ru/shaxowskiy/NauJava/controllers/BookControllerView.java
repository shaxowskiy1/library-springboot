package ru.shaxowskiy.NauJava.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.services.ReservationService;
import ru.shaxowskiy.NauJava.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/books/view")
public class BookControllerView {

    Logger logger = LoggerFactory.getLogger(BookControllerView.class);

    private final ReservationService reservationService;
    private final UserService userService;
    private BookRepository bookRepository;

    @Autowired
    public BookControllerView(BookRepository bookRepository, ReservationService reservationService, UserService userService) {
        this.bookRepository = bookRepository;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String booksListView(Model model){
        logger.info("Показ всех книг ");
        model.addAttribute("books", bookRepository.findAll());
        return "/books/booksList";
    }

    @GetMapping("/{id}")
    public String bookViewId(Model model, @PathVariable("id") Long id){
        logger.info("Показ книги по айди");
        model.addAttribute("book", bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found")));
        return "/books/bookViewId";
    }

    @GetMapping("/reserve/{id}")
    public String bookViewIdForReserve(Model model, @PathVariable("id") Long id){
        logger.info("Показ книги по айди для резервирования");
        model.addAttribute("book", bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found")));
        model.addAttribute("people", userService.findAll());
        model.addAttribute("reservationsWithId", reservationService.findByBookId(id));
        return "/books/bookViewIdReserve";
    }

    @PostMapping("/reserve")
    public String reserveBook(@RequestParam("selectedPerson") Long selectedPersonId,
                              @RequestParam("bookId") Long bookId) {
        logger.info("Метод reserveBook с параметрами: selectedPersonId={}, bookId={}", selectedPersonId, bookId);

        if (selectedPersonId == null || bookId == null) {
            throw new IllegalArgumentException("ID не должен быть null");
        }

        User user = userService.findById(selectedPersonId);
        reservationService.reserveBook(bookId, user.getId());

        return "redirect:/books/view/reserve/" + bookId;
    }
    @PostMapping("/return")
    public String returnBook(@RequestParam("selectedPerson") Long selectedPersonId,
                              @RequestParam("bookId") Long bookId) {
        logger.info("Метод returnBook с параметрами: selectedPersonId={}, bookId={}", selectedPersonId, bookId);

        if (selectedPersonId == null || bookId == null) {
            throw new IllegalArgumentException("ID не должен быть null");
        }

        User user = userService.findById(selectedPersonId);
        reservationService.returnBook(bookId, user.getId());

        return "redirect:/books/view/reserve/" + bookId;
    }
}
