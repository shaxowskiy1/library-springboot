package ru.shaxowskiy.NauJava.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.models.Role;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        userRepository.save(user);
        System.out.println(user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userFound = userRepository.findUserByUsername(username);

        User user = userFound.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getFirstName(), user.getPassword(), mapRoles(user.getRoles()));
    }
    private Collection<GrantedAuthority> mapRoles(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList());
    }
}
