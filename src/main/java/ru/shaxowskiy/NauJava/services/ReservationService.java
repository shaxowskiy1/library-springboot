package ru.shaxowskiy.NauJava.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Reservation;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.models.enums.StatusReserving;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.repositories.ReservationRepository;
import ru.shaxowskiy.NauJava.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @Autowired
    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }

    public List<Reservation> findByBookId(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        return reservationRepository.findAllByBookId(book);
    }

    public void reserveBook(Long bookId, Long userId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        System.out.println("Начало бронирования");
        Reservation reservation = new Reservation();
        reservation.setBookId(book);
        reservation.setUserId(user);
        reservation.setStatus(StatusReserving.TAKEN);
        reservation.setReservationDate(LocalDateTime.now());
        reservationRepository.save(reservation);
    }
    public void returnBook(Long bookId, Long userId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Reservation reservation = reservationRepository.findByBookIdAndUserId(book, user);
        if(reservation == null){
            throw new EntityNotFoundException("Reservation not found");
        }
        System.out.println("Начало возврата");
        reservation.setStatus(StatusReserving.RETURNED);
        reservation.setReturnDate(LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}
