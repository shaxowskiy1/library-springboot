package ru.shaxowskiy.NauJava.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginControllerView {

    @GetMapping
    public String basePage(){

        return "welcome";
    }
}
