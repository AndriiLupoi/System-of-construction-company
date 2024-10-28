package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Category;
import com.example.My_Course_Project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Логіка пошуку категорій
    public List<Category> searchCategories(String query) {
        try {
            Integer id = Integer.parseInt(query);
            return categoryRepository.findByNameContainingOrDescriptionContaining(id.toString(), "");
        } catch (NumberFormatException e) {
            return categoryRepository.findByNameContainingOrDescriptionContaining(query, query);
        }
    }

    public void saveCategory(String name, String description) {
        // Створення нового об'єкта категорії
        Category category = new Category();

        // Встановлення значень полів
        category.setName(name);
        category.setDescription(description);

        // Збереження об'єкта у базі даних
        categoryRepository.save(category);
    }

    public void deleteCategoryById(int id) {
        categoryRepository.deleteById(id);
    }

}
