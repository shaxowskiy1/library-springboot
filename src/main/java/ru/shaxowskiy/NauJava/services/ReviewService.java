package ru.shaxowskiy.NauJava.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Review;
import ru.shaxowskiy.NauJava.models.User;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.repositories.ReviewRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReviewService {

    private final BookRepository bookRepository;
    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public List<Review> findByBookId(Book book) {
        return reviewRepository.findByBookIdOrderByCreatedAtDesc(book).orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    @Transactional
    public void addReview(Review review, User user, Book book) {
        review.setId(null);

        review.setCreatedAt(LocalDateTime.now());
        review.setUserId(user);
        review.setBookId(book);
        log.info("Объект review перед сохранением: {}", review);

        reviewRepository.save(review);
        log.info("Объект review после сохранения: {}", review);
    }
}

