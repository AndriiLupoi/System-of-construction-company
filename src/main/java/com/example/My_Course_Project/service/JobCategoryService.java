package com.example.My_Course_Project.service;

import com.example.My_Course_Project.model.JobCategory;
import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.repository.JobCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;


@Service
public class JobCategoryService {
    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    public List<JobCategory> getAllJobCategory() {
        return jobCategoryRepository.findAll();
    }

    public void deleteJobCategoryById(int id) {
        if (!jobCategoryRepository.existsById(id)) {
            throw new EntityNotFoundException("JobCategory with id " + id + " not found");
        }
        jobCategoryRepository.deleteById(id);
    }

    public void saveJobCategory(String name, String description) {
        // Створення нового об'єкта JobCategory
        JobCategory jobCategory = new JobCategory();

        // Встановлення значень полів
        jobCategory.setName(name);
        jobCategory.setDescription(description);

        // Збереження об'єкта у базі даних
        jobCategoryRepository.save(jobCategory);
    }

    // Логіка пошуку категорій робіт
    public List<JobCategory> searchJobCategories(String query) {
        List<JobCategory> result = new ArrayList<>();
        try {
            // Спробуємо перетворити запит у ціле число для ID
            Integer id = Integer.parseInt(query);
            // Якщо ID знайдено, додаємо його до результату
            jobCategoryRepository.findById(id).ifPresent(result::add);
        } catch (NumberFormatException e) {
            // Якщо не вдається перетворити в ID, шукаємо за назвою або описом
            result = jobCategoryRepository.findByNameContainingOrDescriptionContaining(query, query);
        }
        return result;
    }

}
