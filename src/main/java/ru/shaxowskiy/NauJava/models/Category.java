package ru.shaxowskiy.NauJava.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Класс Category представляет сущность Категорий книг
 * Он содержит информацию о книге: уникальный идентификатор сущности, название и описание категории
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", unique = true)
    private String title;

    @Size(max = 500)
    @Column(name = "description")
    private String descriptions;

}

