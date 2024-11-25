package ru.shaxowskiy.NauJava.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.shaxowskiy.NauJava.models.Book;
import ru.shaxowskiy.NauJava.models.Category;

import java.util.List;

@Repository
public class    BookRepositoryCustomImpl implements BookRepositoryCustom{

    private EntityManager entityManager;

    @Autowired
    public BookRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     *
     * @param author ватор книги
     * @param title название книги
     * @return
     */
    @Override
    public List<Book> findBooksByAuthorOrTitle(String author, String title) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> bookRoot = criteriaQuery.from(Book.class);

        Predicate authorPredicate = criteriaBuilder.equal(bookRoot.get("author"), author);
        Predicate titlePredicate = criteriaBuilder.equal(bookRoot.get("title"), title);

        Predicate combinedPredicate = criteriaBuilder.or(authorPredicate, titlePredicate);

        criteriaQuery.select(bookRoot).where(combinedPredicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     *
     * @param title название категории
     * @return
     */
    @Override
    public List<Book> findBooksByCategory(String title) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

        Root<Book> bookRoot = criteriaQuery.from(Book.class);
        Join<Book, Category> categoryJoin = bookRoot.join("category", JoinType.INNER);

        Predicate categoryTitlePredicate = criteriaBuilder.equal(categoryJoin.get("title"), title);

        criteriaQuery.select(bookRoot).where(categoryTitlePredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
