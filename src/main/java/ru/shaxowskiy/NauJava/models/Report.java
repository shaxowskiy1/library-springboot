package ru.shaxowskiy.NauJava.models;


import jakarta.persistence.*;
import lombok.*;
import ru.shaxowskiy.NauJava.models.enums.Status;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(columnDefinition = "text")
    private String description;
}
