package ru.shaxowskiy.NauJava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.NauJava.models.Report;
import ru.shaxowskiy.NauJava.repositories.ReportRepository;
import ru.shaxowskiy.NauJava.services.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private ReportService reportService;
    private ReportRepository reportRepository;

    @Autowired
    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    /**
     * В контроллере должен быть метод, который создает отчет и
     * запускает процесс его формирования. Метод должен возвращать id отчета
     * и НЕ должен дожидаться окончания формирования отчета.
     * @return
     */
    @GetMapping("/generate")
    public String generateReport(){
        reportService.generateReportAsync();
        return "Сгенерирован отчёт" + reportRepository.count();
    }
    @GetMapping("/{id}")
    public Report getReport(@PathVariable("id") int id) throws Exception {
        return reportService.getReport(id);
    }
}
