package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.shaxowskiy.NauJava.models.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    void deleteCategoryByTitle(String title);
}
