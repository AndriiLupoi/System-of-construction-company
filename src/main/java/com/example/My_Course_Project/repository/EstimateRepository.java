package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Integer> {
    List<Estimate> findByMaterialContaining(String material);

    List<Estimate> findByProjectId(int projectId);

    List<Estimate> findByCost(double cost);

    List<Estimate> findByQuantity(int quantity);

    List<Estimate> findByMaterialContainingAndProjectIdAndCost(String material, int projectId, double cost);


    @Query("SELECT p.id FROM Project p WHERE p.name = ?1")
    Integer findProjectIdByName(String projectName);
}
