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
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book bookId;

    @Column(name = "reservation_date", updatable = false)
    private LocalDateTime reservationDate;

    @Column(name = "return_date", updatable = false)
    private LocalDateTime returnDate;

    @Column(name = "status")
    private String status;

}
