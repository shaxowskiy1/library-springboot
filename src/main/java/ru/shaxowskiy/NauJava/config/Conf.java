package ru.shaxowskiy.NauJava.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class Conf {

    @Autowired
    private CommandProcessor commandProccessor;

    @PostConstruct
    public void commandLineScanner() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите команду в виде 'create + 5 параметров, иначе - exit'");
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input.trim())) {
                    System.out.println("Выход из программы...");
                    break;
                }
                commandProccessor.proccessCommand(input);
            }

        }
    }
}
