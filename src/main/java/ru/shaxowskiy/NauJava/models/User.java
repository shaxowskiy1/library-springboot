package ru.shaxowskiy.NauJava.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "First name should not be empty")
    @Size(max = 50, message = "Write please characters less than 50 characters")
    @Column(name = "first_name", unique = true, nullable = false)
    private String firstName;

    @NotNull(message = "Last name should not be empty")
    @Size(max = 50, message = "Write please characters less than 50 characters")
    @Column(name = "last_name", unique = true, nullable = false)
    private String lastName;

    @NotNull(message = "Password should not be empty")
    @Size(min = 6, message = "Password should be longer than 6 characters")
    private String password;

    @Email(message = "Write please email in format _@_._, where '_' - somebody symbols")
    @Column(name = "email", unique = true)
    private String email;


    @Column(name = "phone")
    private Long phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
