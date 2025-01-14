package ru.shaxowskiy.NauJava.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import ru.shaxowskiy.NauJava.models.enums.Role;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private MailSenderService mailSenderService;

    @Autowired
    public UserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
    }

    /**
     * Сохранение юзера в БД с подтервеждением через почтовый ящик
     * @param user объекта пользователя {@link User}
     * @throws Exception
     */
    @Transactional
    public void addUser(User user) throws Exception {
        Optional<User> userByUsername = userRepository.findUserByUsername(user.getUsername());

        if(userByUsername.isPresent()){
            throw new Exception("user already exist");
        }
        user.setRoles(Collections.singleton(Role.USER));
        System.out.println(user.getRoles());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActivationCode(UUID.randomUUID().toString());
        User savedUser = userRepository.save(user);
        log.info("User saved with id {} and username {}", savedUser.getId(), savedUser.getUsername());


        if (!StringUtils.isEmpty(user.getEmail().trim())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Library. Please visit the following link to verify your email: http://localhost:8080/auth/activate/%s",
                    user.getUsername(), user.getActivationCode()
            );
            System.out.println("Sending email to: " + user.getEmail());
            try {
                mailSenderService.send(user.getEmail().trim(), "Activation code", message);
            } catch (Exception e){
                System.err.println("Failed send message " + e.getMessage());
            }
        } else {
            System.out.println("Email is empty, not sending email.");
        }

    }
    /**
     * Загружает пользователя по имени пользователя.
     *
     * Этот метод ищет пользователя в репозитории по указанному имени пользователя.
     * Если пользователь не найден, выбрасывается исключение {@link UsernameNotFoundException}.
     *
     * @param username имя пользователя, по которому производится поиск.
     * @return объект {@link UserDetails}, содержащий информацию о пользователе.
     *         Если пользователь не найден, выбрасывается {@link UsernameNotFoundException}.
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userFound = userRepository.findUserByUsername(username);

        User user = userFound.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<Role> roles = user.getRoles();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRoles(roles));    }


    private Collection<GrantedAuthority> mapRoles(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList());
    }

    /**
     * Активирует пользователя по предоставленному коду активации.
     *
     * Этот метод ищет пользователя в репозитории по указанному коду активации.
     * Если пользователь с данным кодом не найден, метод возвращает false
     * В противном случае код активации пользователя устанавливается в значение "SUCCESS",
     * и пользователь сохраняется в репозитории. Метод возвращает false
     * при успешной активации пользователя.
     *
     * @param code код активации, используемый для поиска пользователя.
     * @return true если пользователь успешно активирован;
     *         false если пользователь с указанным кодом не найден.
     */
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if(user == null){
            return false;
        }
        user.setActivationCode("SUCCESS");
        userRepository.save(user);
        return true;
    }
}
