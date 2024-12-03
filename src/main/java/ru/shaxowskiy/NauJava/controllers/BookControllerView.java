package ru.shaxowskiy.NauJava.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Review;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.services.BookService;
import ru.shaxowskiy.NauJava.services.ReservationService;
import ru.shaxowskiy.NauJava.services.ReviewService;
import ru.shaxowskiy.NauJava.services.UserService;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/books/view")
public class BookControllerView {

    private final BookService bookService;
    private final ReviewService reviewService;
    private final ReservationService reservationService;
    private final UserService userService;
    private BookRepository bookRepository;

    @Autowired
    public BookControllerView(BookRepository bookRepository, ReservationService reservationService, UserService userService, BookService bookService, ReviewService reviewService) {
        this.bookRepository = bookRepository;
        this.reservationService = reservationService;
        this.userService = userService;
        this.bookService = bookService;
        this.reviewService = reviewService;
    }

    @GetMapping("/list")
    public String booksListView(@RequestParam(value = "title", required = false) String title, Model model){
        log.info("Показ всех книг ");

        model.addAttribute("title", bookRepository.findByTitleContainingIgnoreCase(title));
        model.addAttribute("books", bookService.findAll());
        return "/books/booksList";
    }

    @GetMapping("/{id}")
    public String bookViewId(Model model, @PathVariable("id") Long id){
        log.info("Показ книги по айди");
        Book foundBook = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        model.addAttribute("book", foundBook);
        model.addAttribute("reviews", reviewService.findByBookId(foundBook));
        String currentUrl = "http://localhost:8080/books/view/" + id;
        model.addAttribute("url", currentUrl);
        model.addAttribute("shareText", "Посмотрите что я нашёл на сайте Library!");


        log.info("Книга для установки отзыва равна {}", foundBook);
        model.addAttribute("review", new Review());
        return "/books/bookViewId";
    }

    @PostMapping("/{id}/review")
    public String submitReview(@PathVariable Long id,
                               @ModelAttribute("review") @Valid Review review,
                               BindingResult result,
                               Model model,
                               Principal principal) {
        log.info("Начало: POST submit review");
//        if (result.hasErrors()) {
//            //model.addAttribute("review", review);
//            return "books/bookViewId";
//        }


        User user = userService.findByUsername(principal.getName());
        log.info("Юзер оставляет свой отзыв с именем {}", user);
        Book book = bookService.findById(id);
        log.info("Книга для отзыва: {}", book);
        try {
            reviewService.addReview(review, user, book);
        } catch (OptimisticLockException e) {
            log.error("Ошибка при обновлении отзыва: ", e);
            model.addAttribute("errorMessage", "Отзыв был обновлён другим пользователем. Пожалуйста, повторите попытку.");
            return "books/bookViewId";

        }
        return "redirect:/books/view/" + id;
    }


    @GetMapping("/error")
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(Model model) {
        model.addAttribute("errorMessage", "У вас нет прав для доступа к этому ресурсу");
        return "error";
    }
}
