package ru.shaxowskiy.NauJava.services;

public interface CategoryService {

    /**
     * Удаление категории по её названию
     * @param categoryTitle название категории
     */
    void deleteCategory(String categoryTitle);
}
