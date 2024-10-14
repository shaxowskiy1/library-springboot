package ru.shaxowskiy.NauJava.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "reviews")
public class Review{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
   private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

    @NotNull
    private int rating;

    private String comment;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}

