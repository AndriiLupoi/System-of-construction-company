package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkTypeRepository extends JpaRepository<WorkType, Integer> {
    List<WorkType> findByNameContainingOrDescriptionContaining(String name, String description);

}
