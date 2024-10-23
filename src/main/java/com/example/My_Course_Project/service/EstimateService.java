package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Estimate;
import com.example.My_Course_Project.repository.EstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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



}
