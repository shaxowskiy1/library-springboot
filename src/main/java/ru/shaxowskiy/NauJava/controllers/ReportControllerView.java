package ru.shaxowskiy.NauJava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shaxowskiy.NauJava.repositories.ReportRepository;
import ru.shaxowskiy.NauJava.services.ReportService;

@Controller
@RequestMapping("/reports/view")
public class ReportControllerView {

    private final ReportService reportService;

    @Autowired
    public ReportControllerView(ReportService reportService) {;
        this.reportService = reportService;
    }

    /**
     * Обрабатывает GET-запрос на страницу со всеми отчётами
     * @param model
     * @return
     */
    @GetMapping
    public String reportsListView(Model model){
        model.addAttribute("reports", reportService.findAll());

        return "reportList";
    }


    /**
     * Обрабатывает GET-запрос на страницу c одним отчётом по введённому айди
     * @param model
     * @param id айди отчёта
     * @return
     */
    @GetMapping("/{id}")
    public String getReport(Model model, @PathVariable("id") int id){
        model.addAttribute("report", reportService.findById(id));
        return "report";
    }
}
