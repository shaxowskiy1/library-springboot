package ru.shaxowskiy.NauJava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.shaxowskiy.NauJava.models.Report;
import ru.shaxowskiy.NauJava.repositories.ReportRepository;
import ru.shaxowskiy.NauJava.services.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
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
        return "Сгенерирован отчёт" + reportService.count();
    }

    /**
     * Предоставляет представление в виде отчёта по его id
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}")
    public Report getReport(@PathVariable("id") int id) throws Exception {
        return reportService.findById(id);
    }
}
