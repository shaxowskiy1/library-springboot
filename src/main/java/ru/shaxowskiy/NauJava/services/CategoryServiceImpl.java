package ru.shaxowskiy.NauJava.services;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.repositories.BookRepository;
import ru.shaxowskiy.NauJava.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PlatformTransactionManager platformTransactionManager;

    public CategoryServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, PlatformTransactionManager platformTransactionManager) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    /**
     * Удаление категории и книг по названию категории
     * @param categoryTitle название категории
     */
    @Override
    public void deleteCategory(String categoryTitle) {
        TransactionStatus status = platformTransactionManager.getTransaction(new
                DefaultTransactionDefinition());
        try{
            List<Book> books = bookRepository.findBooksByCategory(categoryTitle);
            for(Book book : books){
                bookRepository.delete(book);
            }
            categoryRepository.deleteCategoryByTitle(categoryTitle);

            platformTransactionManager.commit(status);
        } catch (DataAccessException e){
            platformTransactionManager.rollback(status);
            throw e;
        }
    }
}
