package ru.shaxowskiy.NauJava.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.models.enums.Role;
import ru.shaxowskiy.NauJava.services.BookService;
import ru.shaxowskiy.NauJava.services.CategoryServiceImpl;
import ru.shaxowskiy.NauJava.services.ReservationService;
import ru.shaxowskiy.NauJava.services.UserService;


/**
 * Контроллер для административной панели, отвечающий за управление книгами, резервированием и пользователями.
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BookService bookService;
    private final UserService userService;
    private final ReservationService reservationService;
    private final CategoryServiceImpl categoryServiceImpl;

    @Autowired
    public AdminController(UserService userService, ReservationService reservationService, BookService bookService, CategoryServiceImpl categoryServiceImpl) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.bookService = bookService;
        this.categoryServiceImpl = categoryServiceImpl;
    }

    /**
     * Обрабатывает GET-запрос на отображение списка всех книг для резервирования.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для страницы со списком книг для резервирования.
     */
    @GetMapping("/view/reserve")
    public String getAllInfoAboutReserveBooks(Model model){
        log.info("Показ всех книг для резервирования");
        model.addAttribute("books", bookService.findAll());
        return "/admin/infoReserving";
    }
    /**
     * Обрабатывает GET-запрос на отображение информации о книге для резервирования по её ID.
     *
     * @param model     Модель для передачи данных в представление.
     * @param id        ID книги.
     * @param username  Имя пользователя (необязательный параметр).
     * @return Имя представления для страницы с информацией о книге для резервирования.
     */
    @GetMapping("/view/reserve/{id}")
    public String bookViewIdForReserve(Model model, @PathVariable("id") Long id,
                                       @RequestParam(value = "username", required = false) String username){
        log.info("Показ книги по айди для резервирования");
        log.info("Страница для запрашиваемой книги с id {}", id);
        Book book = bookService.findById(id);

        model.addAttribute("book", book);
        model.addAttribute("people", userService.findAll());
        model.addAttribute("reservationsWithId", reservationService.findByBookId(id));
        return "/books/bookViewIdReserve";
    }

    /**
     * Обрабатывает POST-запрос на резервирование книги администратором.
     *
     * @param selectedPersonId ID пользователя, на которого резервируют книгу.
     * @param bookId           ID книги, которую резервируют.
     * @return Редирект на страницу с информацией о книге.
     */
    @PostMapping("/reserve")
    public String reserveBook(@RequestParam("selectedPerson") Long selectedPersonId,
                              @RequestParam("bookId") Long bookId) {
        log.info("Метод reserveBook с параметрами: selectedPersonId={}, bookId={}", selectedPersonId, bookId);

        User user = userService.findById(selectedPersonId);
        if (user == null) {
            log.error("Пользователь с ID {} не найден", selectedPersonId);
            return "redirect:/error?message=User not found";
        }

        log.info("Метод reserveBook с параметрами: user={}", user);

        Book book = bookService.findById(bookId);
        if (book == null) {
            log.error("Книга с ID {} не найдена", bookId);
            return "redirect:/error?message=Book not found";
        }

        log.info("Метод reserveBook с параметрами: book={}", book);
        reservationService.reserveBook(book, user);
        return "redirect:/admin/view/reserve/" + bookId;
    }


    /**
     * Обрабатывает POST-запрос на возврат книги администратором.
     *
     * @param selectedPersonId ID пользователя за которого возвращают книгу.
     * @param bookId           ID книги, которая возвращается.
     * @return Редирект на страницу с информацией о книге.
     * @throws IllegalArgumentException Если ID пользователя или книги не указаны.
     */
    @PostMapping("/return")
    public String returnBook(@RequestParam("selectedPerson") Long selectedPersonId,
                             @RequestParam("bookId") Long bookId) {
        log.info("Метод returnBook с параметрами: selectedPersonId={}, bookId={}", selectedPersonId, bookId);

        if (selectedPersonId == null || bookId == null) {
            throw new IllegalArgumentException("ID не должен быть null");
        }

        User user = userService.findById(selectedPersonId);
        reservationService.returnBook(bookId, user.getId());

        return "redirect:/admin/view/reserve/" + bookId;
    }

    /**
     * Обрабатывает GET-запрос на отображение административной панели.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для административной панели.
     */
    @GetMapping
    public String getAdminPanel(Model model){
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryServiceImpl.findAll());
        return "/admin/adminPanel";
    }

    /**
     * Обрабатывает POST-запрос на добавление новой книги.
     *
     * @param book           Добавляемая книга.
     * @param bindingResult Результат валидации данных книги.
     * @return Имя представления или редирект на административную панель.
     */
    @PostMapping
    public String addBook(@ModelAttribute("book")Book book,
                          BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/admin/adminPanel";
        }
        bookService.addBook(book);
        return "redirect:/admin";
    }

    /**
     * Обрабатывает GET-запрос на отображение списка всех пользователей.
     *
     * @param model Модель для передачи данных в представление.
     * @return Имя представления для страницы со списком пользователей.
     */
    @GetMapping("/userList")
    public String getUserList(Model model){
        log.info("Метод getUserList");
        model.addAttribute("users", userService.findAll());
        return "/admin/usersList";
    }

    /**
     * Обрабатывает GET-запрос на отображение информации о пользователе по его ID.
     *
     * @param model Модель для передачи данных в представление.
     * @param id    ID пользователя.
     * @return Имя представления для страницы с информацией о пользователе.
     */
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

    /**
     * Обрабатывает POST запрос на нзначение пользователя на роль администратора со стороны администратора
     * @param id айди пользователя
     * @return
     */
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

    /**
     * Обрабатывает POST запрос на разжалование пользователя с роли администратора со стороны администратора
     * @param id
     * @return
     */
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


