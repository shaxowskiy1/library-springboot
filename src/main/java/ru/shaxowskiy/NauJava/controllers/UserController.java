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

    private final UserService userService;
    private final ReservationService reservationService;
    private UserDetailsService userDetailsService;


    @Autowired
    public UserController(UserDetailsService userDetailsService, UserService userService, ReservationService reservationService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @GetMapping("/registration")
    public String getRegistration(@ModelAttribute("user") User user) {
        return "registration";
    }

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

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        System.out.println("in activate method");
        boolean isActivated = userDetailsService.activateUser(code);

        String messageAboutAuthorize = isActivated ? "User successfully activated" : "Activation code is not found";
        model.addAttribute("message", messageAboutAuthorize);
        return "login";
    }

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
