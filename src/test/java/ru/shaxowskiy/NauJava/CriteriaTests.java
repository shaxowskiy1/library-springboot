package ru.shaxowskiy.NauJava;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shaxowskiy.NauJava.dao.BookRepositoryCustomImpl;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Category;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.repositories.CategoryRepository;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class CriteriaTests {

    private BookRepositoryCustomImpl bookRepositoryCustom;
    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public CriteriaTests(BookRepositoryCustomImpl bookRepositoryCustom, BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepositoryCustom = bookRepositoryCustom;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Test
    @Transactional
    void findBookByAuthorOrTitle(){
        Book book = new Book();
        String title = UUID.randomUUID().toString();
        book.setTitle(title);
        String author = UUID.randomUUID().toString();
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);

        List<Book> foundBookByTitle = bookRepositoryCustom.findBooksByAuthorOrTitle(null, title);
        List<Book> foundBookByAuthor = bookRepositoryCustom.findBooksByAuthorOrTitle(author, null);



        Assertions.assertNotNull(foundBookByTitle);
        Assertions.assertNotNull(foundBookByAuthor);
        Assertions.assertEquals(foundBookByAuthor, foundBookByTitle);
    }

    @Test
    @Transactional
    void findBooksByCategory(){
        Category category = new Category();
        String title1 = UUID.randomUUID().toString();
        category.setTitle(title1);
        categoryRepository.save(category);

        Book book = new Book();
        String title2 = UUID.randomUUID().toString();
        book.setTitle(title2);
        book.setCategory(category);
        String author1 = UUID.randomUUID().toString();
        book.setAuthor(author1);
        bookRepository.save(book);

        Book book1 = new Book();
        String title3 = UUID.randomUUID().toString();
        book1.setTitle(title3);
        String author2 = UUID.randomUUID().toString();
        book1.setAuthor(author2);
        book1.setCategory(category);
        bookRepository.save(book1);
        List<Book> foundBook = bookRepositoryCustom.findBooksByCategory(title1);
        System.out.println(foundBook);
        Assertions.assertNotNull(foundBook);
        Assertions.assertEquals(book.getTitle(), foundBook.getFirst().getTitle());
        Assertions.assertEquals(book1.getTitle(), foundBook.get(1).getTitle());

    }
}