package ru.shaxowskiy.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.service.BookService;

import java.util.List;

@Component
public class CommandProcessor {

    private final BookService bookService;
    private final List<Book> bookContainer;

    @Autowired
    public CommandProcessor(BookService bookService, List<Book> bookContainer) {
        this.bookService = bookService;
        this.bookContainer = bookContainer;
    }

    public void proccessCommand(String input){
        String[] cmd = input.split(" ");

        switch (cmd[0]) {
            // create 1 Java17 1234567890 2000 true
            case "create" -> {
                bookService.createBook(Integer.valueOf(cmd[1]), cmd[2], Long.valueOf(cmd[3]), Integer.valueOf(cmd[4]), Boolean.valueOf(cmd[5]));
                System.out.println("Добавлена позиция с данными - id: " + cmd[1] + ", название: " + cmd[2] + ", уникальный номер: " + cmd[3] +
                        ", год публикации " + cmd[4] + ", наличие в библиотеке: " + cmd[5]);
            }
            case "read" ->
            {
                Book book = bookContainer.get(Integer.valueOf(cmd[1]));
                bookService.findById(Integer.valueOf(cmd[1]));
                System.out.println("Книга под данным номером - " + book.toString());
            }
            //реализовать update и пофиксить сдвиг на 1
            // update 1 Java17 1234567890 2000 true
            case "update" -> {
                Book book = bookContainer.get(Integer.valueOf(cmd[1]));
                bookService.updateBook(Integer.valueOf(cmd[1]), cmd[2], Long.valueOf(cmd[3]), Integer.valueOf(cmd[4]), Boolean.valueOf(cmd[5]));
                System.out.println("Книга " + book.toString() + " была заменена на --> id: " + cmd[1] + ", название: " + cmd[2] + "\n уникальный номер: " + cmd[3] +
                ", год публикации " + cmd[4] + ", наличие в библиотеке: " + cmd[5]);
            }
            case "delete" ->
            {
                bookService.deleteById(Integer.valueOf(cmd[1]));
                System.out.println("Книга под номером " + cmd[1] + " была удалена");
            }
            default -> System.out.println("Команда не найдена..");
        }

    }

}
