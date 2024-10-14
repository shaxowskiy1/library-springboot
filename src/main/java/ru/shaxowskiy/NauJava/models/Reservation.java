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
    private Long userId;

    @NotNull
    private Long bookId;

    @Column(name = "reservation_date", updatable = false)
    private LocalDateTime reservationDate;

    @Column(name = "return_date", updatable = false)
    private LocalDateTime returnDate;

    private String status;

}
