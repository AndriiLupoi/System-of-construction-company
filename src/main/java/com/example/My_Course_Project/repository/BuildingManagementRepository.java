package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.BuildingManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingManagementRepository extends JpaRepository<BuildingManagement, Integer> {
}
