package ru.shaxowskiy.NauJava.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shaxowskiy.NauJava.models.Category;

@RepositoryRestResource(path = "categories")
public interface CategoryRepository extends CrudRepository<Category, Long> {

    void deleteCategoryByTitle(String title);
}
