package ru.shaxowskiy.NauJava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.shaxowskiy.NauJava.services.ReservationService;


@Controller
public class ReservationControllerView {

    public ReservationService reservationService;

    @Autowired
    public ReservationControllerView(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/books/view/reserve")
    public String getListReservations(Model model){
        model.addAttribute("reservations", reservationService.findAll());
        return "reservations/reservationsList";
    }

}
