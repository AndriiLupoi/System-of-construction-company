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
import java.util.Optional;


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
            Integer id = Integer.parseInt(query);
            jobCategoryRepository.findById(id).ifPresent(result::add);
        } catch (NumberFormatException e) {
            result = jobCategoryRepository.findByNameContainingOrDescriptionContaining(query, query);
        }
        return result;
    }

    public JobCategory findJobCategoryById(int id) {
        return jobCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("JobCategory not found"));
    }

    public void updateJobCategoryById(int id, JobCategory jobCategory) {
        Optional<JobCategory> existingJobCategory = jobCategoryRepository.findById(id);
        if (existingJobCategory.isPresent()) {
            JobCategory jobCategoryToUpdate = existingJobCategory.get();
            jobCategoryToUpdate.setName(jobCategory.getName());
            jobCategoryToUpdate.setDescription(jobCategory.getDescription());
            jobCategoryRepository.save(jobCategoryToUpdate);
        } else {
            throw new EntityNotFoundException("JobCategory with id " + id + " not found");
        }
    }

}
