package ru.shaxowskiy.NauJava;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.repositories.UserRepository;
import ru.shaxowskiy.NauJava.services.ReportService;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class ReportTests {

    private ReportService reportService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ReportTests(ReportService reportService) {
        this.reportService = reportService;
    }

    @Test
    public void countUser(){
        reportService.generateReportAsync();
    }
}
