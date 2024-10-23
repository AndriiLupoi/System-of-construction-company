package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Integer> {
    // Пошук за матеріалом
    List<Estimate> findByMaterialContaining(String material);

    // Пошук за ID проекту
    List<Estimate> findByProjectId(int projectId);

    // Пошук за вартістю
    List<Estimate> findByCost(double cost);

    List<Estimate> findByQuantity(int quantity);

    // Пошук за всіма полями
    List<Estimate> findByMaterialContainingAndProjectIdAndCost(String material, int projectId, double cost);
}
