package ru.shaxowskiy.NauJava.config;

import ru.shaxowskiy.NauJava.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Configuration
@ComponentScan
public class Config {

    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public List<Book> bookContainer() {
        return new ArrayList<>();
    }

}


