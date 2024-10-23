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

}
