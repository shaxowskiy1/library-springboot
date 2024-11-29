package ru.shaxowskiy.NauJava.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private MailSenderService mailSenderService;


    @Autowired
    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository, UserRepository userRepository, MailSenderService mailSenderService) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.mailSenderService = mailSenderService;
    }

    /**
     * получить все резервации
     * @return список книг
     */
    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }

    /**
     * Найти все резервации книг по айди книги
     * @param bookId айди книги
     * @return список зарезервированных книг
     */
    public List<Reservation> findByBookId(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        return reservationRepository.findAllByBookId(book);
    }

    /**
     * Метод резервирования книг, резервации присваиваются айди книги и юзера, статус "Взята"
     * и время резервирования
     * @param bookId айди желаемой книги
     * @param userId айди юзера
     */
    public void reserveBook(Long bookId, Long userId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Reservation reservation = new Reservation();
        reservation.setBookId(book);
        reservation.setUserId(user);
        reservation.setStatus(StatusReserving.TAKEN);
        reservation.setReservationDate(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    /**
     * Возврат книг после резервации
     * @param bookId айди зарезервированной книги
     * @param userId айди юзера зарезервировавшего книгу
     */
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

    /**
     * Метод, который проверяет даты возврата книг у пользователей.
     * Этот метод выполняется каждый день в 10:00.
     * Если дата возврата книги истекает через 3 дня, пользователю отправляется уведомление.
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void checkReturnDate(){
        List<Reservation> reservations = reservationRepository.findAll();

        for(Reservation reservation : reservations){
            LocalDateTime returnDate = reservation.getReturnDate();
            if(returnDate != null && returnDate.equals(LocalDateTime.now().plusDays(3))){
                User user = reservation.getUserId();
                sendMessAboutReturn(user, returnDate);
            }
        }
    }
    /**
     * Метод для отправки уведомления пользователю о необходимости возврата книги.
     *
     * @param user       Пользователь, которому отправляется уведомление.
     * @param returnDate Дата возврата книги.
     */
    private void sendMessAboutReturn(User user, LocalDateTime returnDate) {

        String subject = "Напоминание о возврате книги в библиотеку 'Duke'";
        String message = "Уважаемый " + user.getUsername() + ",\n\n" +
                "Напоминаем вам о необходимости вернуть книгу до " + returnDate +
                "Пожалуйста, не забудьте вернуть её в библиотеку" + "\n\n"
                + "С уважением, библиотека 'Duke'";
        mailSenderService.send(user.getEmail(), subject, message);
    }
}
