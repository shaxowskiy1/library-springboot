package ru.shaxowskiy.NauJava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shaxowskiy.NauJava.repositories.ReportRepository;

@Controller
@RequestMapping("/reports/view")
public class ReportControllerView {

    private ReportRepository reportRepository;

    @Autowired
    public ReportControllerView(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Обрабатывает GET-запрос на страницу со всеми отчётами
     * @param model
     * @return
     */
    @GetMapping
    public String reportsListView(Model model){
        model.addAttribute("reports", reportRepository.findAll());

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
        model.addAttribute("report", reportRepository.findById(id).orElse(null));
        return "report";
    }
}
