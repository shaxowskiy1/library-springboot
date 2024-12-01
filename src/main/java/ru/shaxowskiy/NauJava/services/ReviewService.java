package ru.shaxowskiy.NauJava.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Review;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.repositories.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {

    private final BookRepository bookRepository;
    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public List<Review> findByBookId(Book book){
        return reviewRepository.findByBookId(book).orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    @Transactional
    public void addReview(Review review){
        reviewRepository.save(review);
    }
}
