package ru.shaxowskiy.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Category;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.repositories.CategoryRepository;
import ru.shaxowskiy.NauJava.services.CategoryServiceImpl;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class TransOpTest {

    private CategoryServiceImpl categoryServiceImpl;
    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public TransOpTest(CategoryServiceImpl categoryServiceImpl, BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.categoryServiceImpl = categoryServiceImpl;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Test
    void testDeleteBook(){
        Category categoryBook = new Category();
        categoryBook.setTitle(UUID.randomUUID().toString());
        categoryRepository.save(categoryBook);

        Book book1 = new Book();
        book1.setAuthor(UUID.randomUUID().toString());
        book1.setTitle(UUID.randomUUID().toString());
        book1.setCategory(categoryBook);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setAuthor(UUID.randomUUID().toString());
        book2.setTitle(UUID.randomUUID().toString());
        book2.setCategory(categoryBook);
        bookRepository.save(book2);

        categoryServiceImpl.deleteCategory(categoryBook.getTitle());

        Optional<Category> foundCategory = categoryRepository.findById(categoryBook.getId());
        Assertions.assertTrue(foundCategory.isEmpty());

        Optional<Book> foundBook1 = bookRepository.findById(book1.getId());
        Assertions.assertTrue(foundBook1.isEmpty());

        Optional<Book> foundBook2 = bookRepository.findById(book1.getId());
        Assertions.assertTrue(foundBook2.isEmpty());


    }
}
