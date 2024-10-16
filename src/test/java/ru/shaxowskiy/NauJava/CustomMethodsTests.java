package ru.shaxowskiy.NauJava;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Category;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.repositories.CategoryRepository;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class CustomMethodsTests {

    public BookRepository bookRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public CustomMethodsTests(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Test
    @Transactional
    public void testFindBooksByAuthorOrTitle(){
        Book book = new Book();
        String title = UUID.randomUUID().toString();
        book.setTitle(title);
        String author = UUID.randomUUID().toString();
        book.setAuthor(author);
        bookRepository.save(book);

        List<Book> foundBookByTitle = bookRepository.findBooksByAuthorOrTitle(null, title);
        List<Book> foundBookByAuthor = bookRepository.findBooksByAuthorOrTitle(author, null);

        Assertions.assertNotNull(foundBookByTitle);
        Assertions.assertNotNull(foundBookByAuthor);
        Assertions.assertEquals(foundBookByAuthor, foundBookByTitle);
    }


    @Test
    @Transactional
    public void testFindBooks(){
        Category category = new Category();
        String title = UUID.randomUUID().toString();
        category.setTitle(title);
        categoryRepository.save(category);

        Book book = new Book();
        book.setTitle(UUID.randomUUID().toString());
        book.setCategory(category);
        book.setAuthor(UUID.randomUUID().toString());
        bookRepository.save(book);

        Book book1 = new Book();
        book1.setTitle(UUID.randomUUID().toString());
        book1.setAuthor(UUID.randomUUID().toString());
        book1.setCategory(category);
        bookRepository.save(book1);
        List<Book> foundBook = bookRepository.findBooksByCategory(title);
        Assertions.assertNotNull(foundBook);
        Assertions.assertEquals(book.getTitle(), foundBook.getFirst().getTitle());
        Assertions.assertEquals(book1.getTitle(), foundBook.get(1).getTitle());

    }
}
