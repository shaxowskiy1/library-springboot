package ru.shaxowskiy.NauJava.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.shaxowskiy.NauJava.models.enums.Role;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Класс User представляет сущность Пользователь
 * Он содержит информацию о книге: уникальный идентификатор сущности,
 * уникальный никнейм, имя фамилию, почтовый ящик, активационный код для подтверждения,
 * дату создания аккаунта и роль
 */
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

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 3, max = 50, message = "Username name must be between 3 and 50 characters in length")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "First name should not be empty")
    @Size(min = 3, max = 50, message = "First name name must be between 3 and 50 characters in length")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters in length")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 6, message = "Password should be longer than 6 characters")
    private String password;

    @Email(message = "Write please email in format _@_._, where '_' - somebody symbols")
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;
}
