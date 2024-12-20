package com.example.My_Course_Project.service;

import com.example.My_Course_Project.model.WorkType;
import com.example.My_Course_Project.repository.ReportRepository;
import com.example.My_Course_Project.repository.WorkTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkTypeService {

    @Autowired
    private WorkTypeRepository workTypeRepository;
    @Autowired
    private ReportRepository reportRepository;

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

    public void deleteWorkTypeById(int id) {
        workTypeRepository.deleteById(id);
    }

    public WorkType findWorkTypeById(Integer workTypeId) {
        return workTypeRepository.findById(workTypeId).orElseThrow(() -> new IllegalArgumentException("Invalid workType ID: " + workTypeId));
    }

    public void updateWorkTypeById(int id, WorkType workType) {
        Optional<WorkType> existingWorkType = workTypeRepository.findById(id);
        if (existingWorkType.isPresent()) {
            WorkType workTypeToUpdate = existingWorkType.get();
            workTypeToUpdate.setName(workType.getName());
            workTypeToUpdate.setDescription(workType.getDescription());
            workTypeRepository.save(workTypeToUpdate);
        } else {
            throw new EntityNotFoundException("WorkType with id " + id + " not found");
        }
    }
}
