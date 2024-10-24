package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.WorkType;
import com.example.My_Course_Project.repository.WorkTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkTypeService {

    @Autowired
    private WorkTypeRepository workTypeRepository;

    public List<WorkType> getAllWorkTypes() {
        return workTypeRepository.findAll();
    }

    // Логіка пошуку типів робіт
    public List<WorkType> searchWorkTypes(String query) {
        try {
            Integer id = Integer.parseInt(query);
            return workTypeRepository.findByNameContainingOrDescriptionContaining(id.toString(), "");
        } catch (NumberFormatException e) {
            return workTypeRepository.findByNameContainingOrDescriptionContaining(query, query);
        }
    }

    public void saveWorkType(String name, String description) {
        // Створення нового об'єкта WorkType
        WorkType workType = new WorkType();

        // Встановлення значень полів
        workType.setName(name);
        workType.setDescription(description);

        // Збереження об'єкта у базі даних
        workTypeRepository.save(workType);
    }
}
