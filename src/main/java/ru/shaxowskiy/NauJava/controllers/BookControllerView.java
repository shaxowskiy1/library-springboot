package ru.shaxowskiy.NauJava.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
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

@Controller
@RequestMapping("/books/view")
public class BookControllerView {

    private final BookService bookService;
    private final ReviewService reviewService;
    Logger logger = LoggerFactory.getLogger(BookControllerView.class);

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
        logger.info("Показ всех книг ");

        model.addAttribute("title", bookRepository.findByTitleContainingIgnoreCase(title));
        model.addAttribute("books", bookService.findAll());
        return "/books/booksList";
    }

    @GetMapping("/{id}")
    public String bookViewId(Model model, @PathVariable("id") Long id){
        logger.info("Показ книги по айди");
        Book foundBook = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        model.addAttribute("book", foundBook);
        model.addAttribute("reviews", reviewService.findByBookId(foundBook));
        String currentUrl = "http://localhost:8080/books/view/" + id;
        model.addAttribute("url", currentUrl);
        model.addAttribute("shareText", "Посмотрите что я нашёл на сайте Library!");

        Review reviewForBookWithId = new Review();
        reviewForBookWithId.setBookId(foundBook);
        logger.info("Книга для установки отзыва равна {}", foundBook);
        model.addAttribute("review", reviewForBookWithId);
        return "/books/bookViewId";
    }

    @PostMapping("/{id}")
    public String submitReview(@PathVariable Long id,
                               @ModelAttribute("review") Review review,
                               BindingResult result,
                               Model model,
                               Principal principal) {
        logger.info("Юзер оставляет свой отзыв");
        if (result.hasErrors()) {
            model.addAttribute("review", review);
            return "books/bookViewId";
        }
        User user = userService.findByUsername(principal.getName());
        Book book = bookService.findById(id);

        review.setBookId(book);
        review.setUserId(user);
        try {
            reviewService.addReview(review);
        } catch (OptimisticLockException e) {
            logger.error("Ошибка при обновлении отзыва: ", e);
            model.addAttribute("errorMessage", "Отзыв был обновлён другим пользователем. Пожалуйста, повторите попытку.");
            model.addAttribute("review", review);
            return "books/bookViewId"; // Возвращаемся на страницу

        }
        return "redirect:/books/view/" + id;
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

    @GetMapping("/error")
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(Model model) {
        model.addAttribute("errorMessage", "У вас нет прав для доступа к этому ресурсу");
        return "error";
    }
}
