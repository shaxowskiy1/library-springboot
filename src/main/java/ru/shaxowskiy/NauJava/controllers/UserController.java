package ru.shaxowskiy.NauJava.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.models.enums.Role;
import ru.shaxowskiy.NauJava.services.ReservationService;
import ru.shaxowskiy.NauJava.services.UserDetailsService;
import ru.shaxowskiy.NauJava.services.UserService;

import java.util.Set;

@Controller
@RequestMapping("/auth")
public class UserController {
    // Сервис для работы с данными пользователей
    private final UserService userService;
    // Сервис для работы с резервациями
    private final ReservationService reservationService;
    // Сервис для работы с пользователями
    private UserDetailsService userDetailsService;


    @Autowired
    public UserController(UserDetailsService userDetailsService, UserService userService, ReservationService reservationService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.reservationService = reservationService;
    }

    /**
     *  Обрабатывает GET-запрос на страницу регистрации.
     *  @ModelAttribute("user") создает объект User, связанный с формой регистрации.
     * @param user
     * @return
     */
    @GetMapping("/registration")
    public String getRegistration(@ModelAttribute("user") User user) {
        return "registration";
    }

    /**
     *      Обрабатывает POST-запрос с формы регистрации.
     *      @ModelAttribute("user") извлекает данные пользователя из формы.
     *      @Valid включает валидацию данных пользователя (аннотации @NotBlank, @Size и т.д. в классе User).
     *      BindingResult содержит результаты валидации.
     * @param user регистририрующийся юзер
     * @param bindingResult ошибки валидации
     * @param model модель
     * @return при ошибках валидации возвр. та же страница, иначе редирект при успешной регистрации
     */
    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            userDetailsService.addUser(user);
        } catch (Exception e) {
            model.addAttribute("message", "user already exist");
        }
        return "redirect:login";
    }

    /**
     * Страничка логина
     * @return представление с логином
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Обрабатывает GET-запрос для активации учетной записи по коду активации.
     * @param model модель
     * @param code код с почты
     * @return
     */
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        System.out.println("in activate method");
        boolean isActivated = userDetailsService.activateUser(code);

        String messageAboutAuthorize = isActivated ? "User successfully activated" : "Activation code is not found";
        model.addAttribute("message", messageAboutAuthorize);
        return "login";
    }

    /**
     * Обрабатывает GET-запрос на страницу профиля пользователя.
     * @param model
     * @return
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        User currentUser  = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("reservationByUser", reservationService.findByUserId(currentUser));

        if (currentUser.getRoles().contains(Role.ADMIN)) {
            model.addAttribute("allUsers", userService.findAll());
            model.addAttribute("allReservations", reservationService.findAll());
        }

        return "user/profile";
    }



}
