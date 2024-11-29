package ru.shaxowskiy.NauJava.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Класс Book представляет сущность Книга
 * Он содержит информацию о книге: уникальный идентификатор сущности, название, автора,
 * уникальный идентификатор книги, год публикации книги, кракое описание книги,
 * категорию и количество
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Title should not be empty")
    @Size(max = 200, message = "Title should not be longer than 200 characters")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Author should not be empty")
    @Size(max = 100)
    @Column(name = "author")
    private String author;

    //@NotNull(message = "Isbn should not be empty")
    @Min(value = 13, message = "Isbn should be has a 13 characters")
    @Max(value = 13, message = "Isbn should be has a 13 characters")
    @Column(name = "isbn")
    private Long isbn;

    @Min(value = 0, message = "Year must be at least 0")
    @Max(value = 2024, message = "Year must be at most 2024")
    private String publishedYear;

    @Size(max = 1000, message = "Description should not be longer than 1000 characters")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Min(value = 0, message = "Quantity should be >0")
    @Column(columnDefinition = "int default 0")
    private int quantity;
}
