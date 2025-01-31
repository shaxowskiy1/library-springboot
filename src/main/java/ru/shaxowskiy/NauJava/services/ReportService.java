package ru.shaxowskiy.NauJava.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Report;
import ru.shaxowskiy.NauJava.models.enums.Status;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.repositories.ReportRepository;
import ru.shaxowskiy.NauJava.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Получить все отчёты
     * @return
     */
    public List<Report> findAll(){
        return reportRepository.findAll();
    }

    /**
     * В сервисе должен быть метод для получения содержимого отчета.
     * @param id уникальный идент отчета
     * @return
     */
    public Report findById(int id) {
        Optional<Report> foundReport = reportRepository.findById(id);
        if(foundReport.isEmpty()){
            try {
                throw new Exception("Report with this id not found");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return foundReport.get();
    }

    /**
     * В сервисе должен быть метод для создания отчета. Метод создает
     * объект в БД и возвращает id объекта. При создании статус отчета по
     * умолчанию имеет значение “создан”.
     */
    public Integer createReport(){
        Report report = new Report();
        report.setStatus(Status.CREATED);
         reportRepository.save(report);

         return report.getId();
    }

    /**
     * В сервисе должен быть асинхронный метод, который будет запускать
     * формирование отчета. Асинхронность достигается с использованием
     * CompletableFuture. В этом методе вычисление количества пользователей
     * должно осуществляться в отдельном потоке с помощью Thread.
     * Аналогично вынести во второй поток получение списка объектов. Не
     * забыть про использование метода join(), чтобы дождаться окончания
     * выполнения потоков. После завершения всех потоков и подсчета времени,
     * в зависимости от результат выполнения операции, статус отчета должен
     * переходить в “завершен” или “ошибка”.
     */
    public void generateReportAsync(){
        long startTime = System.currentTimeMillis();

        Report report = reportRepository.findById(createReport()).orElseThrow(() -> new IllegalArgumentException("Report not found"));

        CompletableFuture<Long> userCount = CompletableFuture.supplyAsync(this::userCountInfo);
        long endTime1 = System.currentTimeMillis();

        CompletableFuture<List<Book>> booksCount = CompletableFuture.supplyAsync(this::booksObjectInfo);

        long endTime2 = System.currentTimeMillis();

        userCount.join();
        booksCount.join();


        String infoUserCount = setUserCount(userCount, report);
        String infoBooks = setBooksInfo(booksCount, report);

        report.setDescription(String.format("%s\n%s\n" +
                        "Время выполнения поиска пользователей: %d мс\n" +
                        "Время выполнения поиска книг: %d мс",
                infoUserCount, infoBooks,
                endTime1 - startTime, endTime2 - startTime));
        System.out.println(report.getDescription());
        reportRepository.save(report);
}

    /**
     * Обработка асинхронного метода получения отчёта по книгам
     * @param booksCount
     * @param report
     * @return
     */
    private String setBooksInfo(CompletableFuture<List<Book>> booksCount, Report report) {
        StringBuilder booksInfo = new StringBuilder();

        booksCount.thenAccept(result -> {
            String resultOfBooks = "Полученный результат для книг: " + result.toString();
            System.out.println(resultOfBooks);
            booksInfo.append(resultOfBooks);
            report.setStatus(Status.COMPLETED);
            reportRepository.save(report);
        }).exceptionally(ex -> {
            System.out.println("При выполнении произошла ошибка при подсчёте книг: " + ex.getMessage());
            report.setStatus(Status.ERROR);
            reportRepository.save(report);
            return null;
        });
        return booksInfo.toString();
    }

    /**
     * Обработка асинхронного метода получения отчёта по юзерам
     * @param userCount
     * @param report
     * @return
     */
    private String setUserCount(CompletableFuture<Long> userCount, Report report) {
        StringBuilder sb = new StringBuilder();

        userCount.thenAccept(result -> {
            String timeGenerationReportForUser = "Полученный результат для пользователей: " + result;
            sb.append(timeGenerationReportForUser);
            System.out.println(timeGenerationReportForUser);
            report.setStatus(Status.COMPLETED);
            reportRepository.save(report);
            report.setDescription(timeGenerationReportForUser);

        }).exceptionally(ex -> {
            String errorMess = "При выполнении произошла ошибка при подсчёте пользователей: " +
                    ex.getMessage();
            sb.append(errorMess);
            System.out.println(errorMess);
            report.setStatus(Status.ERROR);
            report.setDescription(errorMess);
            reportRepository.save(report);
            return null;
        });
        return sb.toString();
    }

    /**
     * Поиск всех книг
     * @return список книг
     */
    private List<Book> booksObjectInfo() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);

        return books;
    }

    /**
     * подсчет количества юзеров
     * @return количество юзеров
     */
    protected Long userCountInfo() {
        return userRepository.count();
    }

    public long count() {
        return reportRepository.count();
    }
}
