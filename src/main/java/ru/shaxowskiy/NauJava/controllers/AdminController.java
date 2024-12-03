package ru.shaxowskiy.NauJava.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.models.enums.Role;
import ru.shaxowskiy.NauJava.services.BookService;
import ru.shaxowskiy.NauJava.services.ReservationService;
import ru.shaxowskiy.NauJava.services.UserService;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
    private final ReservationService reservationService;

    @Autowired
    public AdminController(UserService userService, ReservationService reservationService, BookService bookService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.bookService = bookService;
    }

    @GetMapping("/view/reserve")
    public String getAllInfoAboutReserveBooks(Model model){
        model.addAttribute("books", bookService.findAll());
        return "/admin/infoReserving";
    }

    @GetMapping("/view/reserve/{id}")
    public String bookViewIdForReserve(Model model, @PathVariable("id") Long id){
        log.info("Показ книги по айди для резервирования");
        log.info("Страница для запрашиваемой книги с id {}", id);
        Book book = bookService.findById(id);

        model.addAttribute("book", book);
        model.addAttribute("people", userService.findAll());
        model.addAttribute("reservationsWithId", reservationService.findByBookId(id));
        return "/books/bookViewIdReserve";
    }
    //path?? /view
    @PostMapping("/reserve")
    public String reserveBook(@RequestParam("selectedPerson") Long selectedPersonId,
                              @RequestParam("bookId") Long bookId) {
        log.info("Метод reserveBook с параметрами: selectedPersonId={}, bookId={}", selectedPersonId, bookId);

        if (selectedPersonId == null || bookId == null) {
            throw new IllegalArgumentException("ID не должен быть null");
        }

        User user = userService.findById(selectedPersonId);
        log.info("Метод reserveBook с параметрами: user={}", user);

        Book book = bookService.findById(bookId);
        reservationService.reserveBook(book, user);
        log.info("Метод reserveBook с параметрами: user={}", user);
        return "redirect:/books/view/reserve/" + bookId;
    }
    @PostMapping("/return")
    public String returnBook(@RequestParam("selectedPerson") Long selectedPersonId,
                             @RequestParam("bookId") Long bookId) {
        log.info("Метод returnBook с параметрами: selectedPersonId={}, bookId={}", selectedPersonId, bookId);

        if (selectedPersonId == null || bookId == null) {
            throw new IllegalArgumentException("ID не должен быть null");
        }

        User user = userService.findById(selectedPersonId);
        reservationService.returnBook(bookId, user.getId());

        return "redirect:/books/view/reserve/" + bookId;
    }

    @GetMapping
    public String getAdminPanel(){
        return "/admin/adminPanel";
    }

    @GetMapping("/userList")
    public String getUserList(Model model){
        log.info("Метод getUserList");
        model.addAttribute("users", userService.findAll());
        return "/admin/usersList";
    }

    @GetMapping("/user/{id}")
    public String getUserWithId(Model model, @PathVariable("id") Long id){
        log.info("Id в методе getUserWithId равен {}", id);
        User user = userService.findById(id);

        if (user == null) {
            model.addAttribute("errorMessage", "Пользователь не найден");
        }
        log.info("User для показа профиля: {}", user);
        model.addAttribute("user", user);

        model.addAttribute("reservationForUser", reservationService.findByUserId(user));
        return "/admin/userProfile";
    }

    @PostMapping("/user/{id}/assign-admin")
    public String assignAdmin(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            userService.assignRole(user, Role.ADMIN);
            return "redirect:/admin/user/" + id;
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/user/{id}/revoke-admin")
    public String revokeAdmin(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            userService.assignRole(user, Role.USER);
            return "redirect:/admin/user/" + id;
        } else {
            return "redirect:/error";
        }
    }


}


