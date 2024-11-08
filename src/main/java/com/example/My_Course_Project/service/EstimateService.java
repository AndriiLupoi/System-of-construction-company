package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Estimate;
import com.example.My_Course_Project.model.Schedule;
import com.example.My_Course_Project.repository.EstimateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstimateService {

    @Autowired
    private EstimateRepository estimateRepository;

    public List<Estimate> getAllEstimates() {
        return estimateRepository.findAll();
    }

    // Логіка пошуку кошторисів
    public List<Estimate> searchEstimates(String query) {
        List<Estimate> results = new ArrayList<>();

        try {
            // Спробуємо спочатку розпізнати як число (ID проекту, вартість або кількість)
            Integer id = Integer.parseInt(query);
            results.addAll(estimateRepository.findByProjectId(id)); // Пошук за ID проекту
            results.addAll(estimateRepository.findByCost(id)); // Пошук за вартістю
            results.addAll(estimateRepository.findByQuantity(id)); // Пошук за кількістю
        } catch (NumberFormatException e) {
            // Якщо не число, перевіряємо, чи це матеріал
            String material = query;
            results.addAll(estimateRepository.findByMaterialContaining(material)); // Пошук за матеріалом
        }

        return results;
    }
    public void saveEstimate(int projectId, String material, int quantity, double cost) {
        // Створення нового об'єкта Estimate
        Estimate estimate = new Estimate();

        // Встановлення значень полів
        estimate.setProjectId(projectId);
        estimate.setMaterial(material);
        estimate.setQuantity(quantity);
        estimate.setCost(cost);

        // Збереження об'єкта у базі даних
        estimateRepository.save(estimate);
    }

    public void deleteEstimateById(int estimateId) {
        estimateRepository.deleteById(estimateId);
    }

    public List<Estimate> getEstimatesByProjectName(String projectName) {
        // Припускаємо, що у вас є метод у репозиторії, який знаходить ID проекту за його назвою
        Integer projectId = estimateRepository.findProjectIdByName(projectName);
        return estimateRepository.findByProjectId(projectId);
    }

    public Estimate findEstimateById(int id) {
        return estimateRepository.findById(id).orElseThrow(() -> new RuntimeException("Estimate not found"));
    }

    public void updateEstimateById(int id, Estimate estimate) {
        Optional<Estimate> existingEstimate = estimateRepository.findById(id);

        if (existingEstimate.isPresent()) {
            Estimate estimateToUpdate = existingEstimate.get();
            estimateToUpdate.setProjectId(estimate.getProjectId());
            estimateToUpdate.setMaterial(estimate.getMaterial());
            estimateToUpdate.setQuantity(estimate.getQuantity());
            estimateToUpdate.setCost(estimate.getCost());
            estimateRepository.save(estimateToUpdate);
        } else {
            throw new EntityNotFoundException("Estimate with id " + id + " not found");
        }
    }
}
